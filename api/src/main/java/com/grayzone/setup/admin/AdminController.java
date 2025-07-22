package com.grayzone.setup.admin;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.auth.dto.response.LoginResponseDto;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.token.TokenManager;
import com.grayzone.global.token.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final UserRepository userRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final TokenManager tokenManager;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.nickname}")
  private String adminNickname;

  @PostMapping
  public ResponseEntity<ResponseDataDto<Void>> createAdmin() {

    if (userRepository.existsByNickname(adminNickname)) {
      throw new IllegalArgumentException("server error");
    }

    LegalDistrict mainRegion = legalDistrictRepository
      .findAll(PageRequest.of(0, 1))
      .stream()
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("server error"));

    User admin = User.builder()
      .email(adminEmail)
      .nickname(adminNickname)
      .mainRegion(mainRegion)
      .agreedServiceUse(true)
      .agreedPrivacy(true)
      .agreedLocation(true)
      .build();

    userRepository.save(admin);

    return ResponseEntity.ok(
      ResponseDataDto.from(
        null
      )
    );
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDataDto<LoginResponseDto>> loginAdmin() {
    long validityTime = 1000L * 60 * 60 * 24 * 7;
    User user = userRepository.findByEmail(adminEmail).orElseThrow(() -> new IllegalArgumentException("server error"));

    TokenPair tokenPair = tokenManager.createTokenPair(user.getId(), validityTime);

    return ResponseEntity.ok(
      ResponseDataDto.from(
        LoginResponseDto.builder()
          .accessToken(tokenPair.getAccessToken())
          .refreshToken(tokenPair.getRefreshToken())
          .build()
      )
    );
  }
}
