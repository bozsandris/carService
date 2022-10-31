package com.practice.carservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.carservice.domain.Role;
import com.practice.carservice.domain.User;
import com.practice.carservice.security.JwtConfig;
import com.practice.carservice.service.UserServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final JwtConfig jwtConfig;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/manage/users")
    public ResponseEntity<List<User>>getManagableUsers() {
        return ResponseEntity.ok().body(
                userService.getUsers().stream()
                        .filter(user -> !user.getRoles().contains(new Role("ROLE_ADMIN")))
                        .toList()
        );
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/user{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(userService.getUserByUsername(email));
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveUser(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/revokeRole")
    public ResponseEntity<?> revokeRoleToUser(@RequestBody RoleToUserForm form) {
        userService.revokeRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.isUserRegistered(user.getUsername()))
        {
            return ResponseEntity.badRequest().build();
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/register").toUriString());
        User registeredUser = userService.saveUser(user);
        userService.addRoleToUser(registeredUser.getUsername(), "ROLE_USER");
        return ResponseEntity.created(uri).body(registeredUser);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            throw new RuntimeException("Refresh token is missing");
        }

        try {
            String refresh_token = authorizationHeader.substring(jwtConfig.getTokenPrefix().length());
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String username = decodedJWT.getSubject();
            User user = userService.getUserByUsername(username);
            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(jwtConfig.getAccessTokenExpDate())
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", getUserAuthorityList(user))
                    .sign(algorithm);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", access_token);
            tokens.put("userId", getUserId(user));
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch(Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", exception.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

    private String getUserId(User user) {
        return userService.getUserByUsername(user.getUsername()).getId().toString();
    }

    private List<String> getUserAuthorityList(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Data
    private static class RoleToUserForm {
        private String username;
        private String roleName;
    }
}
