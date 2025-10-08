package com.mpt.journal.config;

import com.mpt.journal.domain.entity.UserEntity;
import com.mpt.journal.domain.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UsersService _users;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(login -> {
            UserEntity user = _users.getUserByLogin(login);
            if (user == null) {
                throw new UsernameNotFoundException("Такой пользователь не существует");
            } else if (user.getBan_rec() != null && user.getBan_rec().getBan_expired().toLocalDate().isBefore(LocalDate.now()))
                throw new AccessDeniedException("User with this login is banned until "
                        + user.getBan_rec().getBan_expired().toLocalDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
                return new org.springframework.security.core.userdetails.User(
                        user.getLogin(),
                        user.getPassword(),
                        !user.getDeleted(),
                        true,
                        true,
                        true,
                        user.getRoles()
                );
        }).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/login", "/registration").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login");
                    httpSecurityFormLoginConfigurer.defaultSuccessUrl("/");
                    httpSecurityFormLoginConfigurer.permitAll();
                    httpSecurityFormLoginConfigurer.permitAll();
                })
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer.permitAll();
                    httpSecurityLogoutConfigurer.disable();

                });

        return http.build();
    }

}
