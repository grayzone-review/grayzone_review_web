package com.grayzone.setup.admin;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final UserRepository userRepository;
  private final LegalDistrictRepository legalDistrictRepository;

  @PostMapping
  public ResponseEntity<ResponseDataDto<Void>> createAdmin() {
    String nickname = "admin1234";
    String email = UUID.randomUUID().toString() + "@up.com";
    LegalDistrict mainRegion = legalDistrictRepository
      .findById(1L)
      .orElseThrow(() -> new IllegalArgumentException("Legal District not found"));

    User admin = User.builder()
      .email(email)
      .nickname(nickname)
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
}
