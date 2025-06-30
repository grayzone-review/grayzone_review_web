package com.grayzone.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TermsResponseDto {
  private final List<TermResponseDto> terms;

  public TermsResponseDto(List<TermResponseDto> terms) {
    this.terms = terms;
  }

  @Getter
  public static class TermResponseDto {
    private boolean isRequired;
    private String term;
    private String url;

    public TermResponseDto(boolean isRequired, String term, String url) {
      this.isRequired = isRequired;
      this.term = term;
      this.url = url;
    }
  }
}
