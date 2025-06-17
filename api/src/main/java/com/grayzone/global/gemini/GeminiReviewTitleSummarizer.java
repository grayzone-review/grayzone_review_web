package com.grayzone.global.gemini;

import com.grayzone.domain.review.ReviewTitleSummarizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeminiReviewTitleSummarizer implements ReviewTitleSummarizer {
  private final RestClient restClient;

  public GeminiReviewTitleSummarizer() {
    this.restClient = RestClient.builder()
      .baseUrl(GeminiConst.BASEURL)
      .build();
  }


  @Override
  public String summarize(String body) {
    GeminiSummarizeRequest request = new GeminiSummarizeRequest(body);
    GeminiSummarizeResponse response = restClient.post()
      .uri(uriBuilder -> uriBuilder
        .path(GeminiConst.PATH)
        .queryParam("key", GeminiConst.KEY)
        .build()
      ).body(request)
      .retrieve()
      .body(GeminiSummarizeResponse.class);

    String text = response.getText();

    return text;
  }
}
