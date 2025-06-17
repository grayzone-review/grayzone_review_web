package com.grayzone.global.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class GeminiSummarizeResponse {
  private List<Candidate> candidates;

  public String getText() {
    if (candidates == null || candidates.isEmpty()) {
      return null;
    }

    Content content = candidates.getFirst().getContent();
    if (content == null || content.getParts() == null || content.getParts().isEmpty()) {
      return null;
    }

    return content.getParts().getFirst().getText();
  }

  @Setter
  @Getter
  public static class Candidate {
    private Content content;
  }

  @Setter
  @Getter
  public static class Content {
    private List<Part> parts;
  }

  @Setter
  @Getter
  public static class Part {
    private String text;
  }
}

