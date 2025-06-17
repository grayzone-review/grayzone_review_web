package com.grayzone.global.gemini;

import lombok.Getter;

import java.util.List;

@Getter
public class GeminiSummarizeRequest {
  private final List<Content> contents;

  public GeminiSummarizeRequest(String text) {
    this.contents = List.of(new Content(text));
  }

  @Getter
  public static class Content {
    private final List<Part> parts;

    public Content(String text) {
      this.parts = List.of(new Part(text));
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
