package com.grayzone.global.filter;

import com.grayzone.global.config.AdminEndpoints;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AdminAuthenticateFilter extends OncePerRequestFilter {
  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Value("${admin.api.key}")
  private String apiKey;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String keyFromRequest = getKeyFromRequest(request);

    if (!StringUtils.hasText(keyFromRequest)) {
      throw new UpException(UpError.ADMIN_PERMISSION_DENIED);
    }

    if (!apiKey.equals(keyFromRequest)) {
      throw new UpException(UpError.ADMIN_PERMISSION_DENIED);
    }

    filterChain.doFilter(request, response);
  }

  private String getKeyFromRequest(HttpServletRequest request) {
    String key = request.getHeader("Authorization");
    if (StringUtils.hasText(key)) {
      return key;
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    List<String> blackList = Stream.concat(
      Arrays.stream(AdminEndpoints.GET),
      Arrays.stream(AdminEndpoints.POST)
    ).toList();

    return blackList.stream().noneMatch(pattern -> pathMatcher.match(pattern, path));
  }
}
