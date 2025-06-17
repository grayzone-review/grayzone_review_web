package com.grayzone.global.gemini;

import org.springframework.beans.factory.annotation.Value;

public class GeminiConst {
  public static final String BASEURL = "https://generativelanguage.googleapis.com";
  public static final String PATH = "/v1beta/models/gemini-1.5-flash:generateContent";

  @Value("${gemini.key}")
  public static String KEY;
}
