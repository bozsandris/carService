package com.practice.carservice.security;

import com.practice.carservice.filter.CustomAuthenticationFilter;
import com.practice.carservice.filter.CustomAuthorizationFilter;
import com.practice.carservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;
    private final UserServiceImpl userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.cors().and()
                .authorizeRequests()
                //order based decline!!!!
                .antMatchers("/*", "/static/**", "/#/*", "/api/v1/register", "/api/v1/token/refresh").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/carpart").hasAnyAuthority("ROLE_MECHANIC")
                .antMatchers(HttpMethod.POST,"/api/v1/carpart").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/v1/repair/manage/**").hasAnyAuthority("ROLE_MECHANIC", "ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/#/login");
        http.addFilter(new CustomAuthenticationFilter(userService, authenticationManagerBean(), jwtConfig));
        http.addFilterBefore(new CustomAuthorizationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
}