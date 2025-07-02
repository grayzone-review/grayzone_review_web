package com.grayzone.global.config;

public class PublicEndpoints {
  public static final String[] GET = {
    "/api/legal-districts/setup",
    "/api/legal-districts",
    "/error"
  };

  public static final String[] POST = {
    "/api/auth/**",
    "/api/users/nickname-verify",
  };
}
