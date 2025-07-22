package com.grayzone.global.config;

public class AdminEndpoints {
  public static final String[] GET = {
    "/api/legal-districts/setup",
    "/api/admin/**",
    "/api/admin"
  };

  public static final String[] POST = {
    "/api/admin/**",
    "/api/admin"
  };
}
