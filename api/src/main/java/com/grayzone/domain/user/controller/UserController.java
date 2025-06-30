package com.grayzone.domain.user.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.user.dto.TermsResponseDto;
import com.grayzone.domain.user.dto.VerifyNicknameDuplicateRequestDto;
import com.grayzone.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users/nickname-verify")
  public ResponseEntity<ResponseDataDto<Void>> verifyNicknameDuplicate(
    @Valid @RequestBody VerifyNicknameDuplicateRequestDto requestDto
  ) {
    userService.verifyNicknameDuplicate(requestDto.getNickname());

    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .body(ResponseDataDto.from(null));
  }

  @GetMapping("/terms")
  public ResponseEntity<ResponseDataDto<TermsResponseDto>> getTerms() {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        new TermsResponseDto(List.of(
          new TermsResponseDto.TermResponseDto(true, "[필수] 서비스 이용 약관 동의", ""),
          new TermsResponseDto.TermResponseDto(true, "[필수] 개인정보 수집 및 이용 동의", ""),
          new TermsResponseDto.TermResponseDto(true, "[필수] 위치 기반 서비스 이용 동의", "")
        ))
      )
    );
  }
}
