package com.practice.carservice.security;

import com.practice.carservice.filter.CustomAuthenticationFilter;
import com.practice.carservice.filter.CustomAuthorizationFilter;
import com.practice.carservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;
    private final UserServiceImpl userService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .cors()
            .and()
            .authorizeRequests(auth -> {
                //order based decline!!!!
                auth.antMatchers("/*", "/static/**", "/api/v1/register", "/api/v1/token/refresh").permitAll();
                auth.antMatchers(HttpMethod.GET,"/api/v1/carpart").hasAnyAuthority("ROLE_MECHANIC");
                auth.antMatchers(HttpMethod.POST,"/api/v1/carpart").hasAnyAuthority("ROLE_ADMIN");
                auth.antMatchers("/api/v1/repair/manage/**").hasAnyAuthority("ROLE_MECHANIC", "ROLE_ADMIN");
                auth.anyRequest().authenticated();
            })
            .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
            });
        http.addFilter(new CustomAuthenticationFilter(userService, authenticationManagerBean(authenticationConfiguration), jwtConfig));
        http.addFilterBefore(new CustomAuthorizationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return provider;
    }
}