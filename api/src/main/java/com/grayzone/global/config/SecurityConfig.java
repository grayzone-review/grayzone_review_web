package com.grayzone.global.config;

import com.grayzone.global.filter.DevelopAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final DevelopAuthenticationFilter developAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET,
          "/api/legal-districts/setup",
          "/api/legal-districts",
          "/api/terms"
        ).permitAll()
        .requestMatchers(HttpMethod.POST,
          "/api/users/nickname-verify"
        ).permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(developAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
