package com.grayzone.global.oauth.apple;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleJWTHeader {
  private String alg;
  private String kid;
}
