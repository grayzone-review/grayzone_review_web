package com.grayzone.global.config;

import com.grayzone.global.exception.CustomAuthenticationEntryPoint;
import com.grayzone.global.filter.AdminAuthenticateFilter;
import com.grayzone.global.filter.JWTAuthenticateFilter;
import com.grayzone.global.filter.UpExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JWTAuthenticateFilter jwtAuthenticateFilter;
  private final AdminAuthenticateFilter adminAuthenticateFilter;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;
  private final UpExceptionHandlerFilter upExceptionHandlerFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, PublicEndpoints.GET).permitAll()
        .requestMatchers(HttpMethod.GET, AdminEndpoints.GET).permitAll()
        .requestMatchers(HttpMethod.POST, AdminEndpoints.POST).permitAll()
        .requestMatchers(HttpMethod.POST, PublicEndpoints.POST).permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(upExceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(adminAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(exception -> exception
        .authenticationEntryPoint(authenticationEntryPoint));

    return http.build();
  }
}
