package com.grayzone.global.gemini;

import lombok.Getter;

import java.util.List;

@Getter
public class GeminiRequest {
  private final List<Content> contents;

  public GeminiRequest(List<Content> contents) {
    this.contents = contents;
  }

  @Getter
  public static class Content {
    private final List<Part> parts;

    public Content(List<Part> parts) {
      this.parts = parts;
    }
  }

  @Getter
  public static class Part {
    private final String text;

    public Part(String text) {
      this.text = text;
    }
  }
}
