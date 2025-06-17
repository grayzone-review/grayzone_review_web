package com.grayzone.global.gemini;

import lombok.Getter;

import java.util.List;

@Getter
public class GeminiSummarizeRequest {
  private final List<Content> contents;

  public GeminiSummarizeRequest(String prompt) {
    this.contents = List.of(new Content(prompt));
  }

  @Getter
  public static class Content {
    private final List<Part> parts;

    public Content(String prompt) {
      this.parts = List.of(new Part(prompt));
    }
  }

  @Getter
  public static class Part {
    private final String prompt;

    public Part(String prompt) {
      this.prompt = prompt;
    }
  }
}
