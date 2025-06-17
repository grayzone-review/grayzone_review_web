package com.grayzone.global.gemini;

import com.grayzone.domain.review.ReviewTitleSummarizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class GeminiReviewTitleSummarizer implements ReviewTitleSummarizer {
  private final RestClient restClient;

  @Value("${gemini.key}")
  private String key;

  public GeminiReviewTitleSummarizer() {
    this.restClient = RestClient.builder()
      .baseUrl(GeminiConst.BASEURL)
      .build();
  }


  @Override
  public String summarize(String body) {
    String summarizeSource = body + "Summarize the following into a Korean review title (max 100 characters). No explanation only return the title in Korean.";
    log.info("summarizeSource: {}", summarizeSource);

    GeminiSummarizeRequest request = new GeminiSummarizeRequest(summarizeSource);

    try {
      GeminiSummarizeResponse response = restClient.post()
        .uri(uriBuilder -> uriBuilder
          .path(GeminiConst.PATH)
          .queryParam("key", key)
          .build()
        )
        .header("Content-Type", "application/json")
        .body(request)
        .retrieve()
        .body(GeminiSummarizeResponse.class);
      return response.getText();
    } catch (Exception e) {
      log.info("error", e);
      throw e;
    }
  }
}
