package com.grayzone.global.filter;

import com.grayzone.global.config.AdminEndpoints;
import com.grayzone.global.config.PublicEndpoints;
import com.grayzone.global.token.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticateFilter extends OncePerRequestFilter {
  private static final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final UserDetailsService userDetailsService;
  private final TokenManager tokenManager;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    String token = getTokenFromRequest(request);

    if (!StringUtils.hasText(token)) {
      throw new ServletException("Missing JWT token");
    }

    if (!tokenManager.validateAccessToken(token)) {
      throw new ServletException("Invalid JWT token");
    }

    String subject = tokenManager.parseSubject(token);
    UserDetails user = userDetailsService.loadUserByUsername(subject);

    Authentication authentication =
      new UsernamePasswordAuthenticationToken(
        user,
        null,
        user.getAuthorities()
      );

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    filterChain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    Stream<String> publicEndpoints = Stream.concat(
      Arrays.stream(PublicEndpoints.GET),
      Arrays.stream(PublicEndpoints.POST)
    );

    Stream<String> adminEndpoints = Stream.concat(
      Arrays.stream(AdminEndpoints.GET),
      Arrays.stream(AdminEndpoints.POST)
    );

    List<String> whitelist = Stream.concat(
      publicEndpoints,
      adminEndpoints
    ).toList();

    return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
  }
}
