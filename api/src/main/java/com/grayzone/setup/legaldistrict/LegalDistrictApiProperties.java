package com.grayzone.setup.legaldistrict;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "legal.district.api")
public class LegalDistrictApiProperties {
  private String baseUrl;
  private String path;
  private String key;
}
