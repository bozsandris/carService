package com.practice.carservice.service;

import com.practice.carservice.domain.Role;
import com.practice.carservice.domain.User;
import com.practice.carservice.repo.RoleRepo;
import com.practice.carservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        log.info("User {} found", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
            authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        log.info("Fetching user with id {}", userId);
        return userOptional
                .orElseThrow(() -> new IllegalStateException("User with id: " + userId + " does not exist"));
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " does not exist"));

        log.info("Fetching user {}", username);
        return user;
    }

    public boolean isUserRegistered(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " does not exist"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role called: " + roleName + " does not exist"));

        log.info("Adding role {} to user {}", roleName, username);
        user.getRoles().add(role);
    }

    public void revokeRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " does not exist"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role called: " + roleName + " does not exist"));

        log.info("Revoking role {} from user {}", roleName, username);
        user.getRoles().remove(role);
    }
}
