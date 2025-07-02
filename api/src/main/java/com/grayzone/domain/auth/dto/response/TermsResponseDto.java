package com.grayzone.domain.auth.dto.response;

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
    private final boolean isRequired;
    private final String term;
    private final String url;
    private final String code;

    public TermResponseDto(boolean isRequired, String term, String url, String code) {
      this.isRequired = isRequired;
      this.term = term;
      this.url = url;
      this.code = code;
    }
  }
}
