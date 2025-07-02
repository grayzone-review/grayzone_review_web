package com.grayzone.domain.auth.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.auth.dto.request.SignUpRequestDto;
import com.grayzone.domain.auth.dto.response.SignUpResponseDto;
import com.grayzone.domain.auth.dto.response.TermsResponseDto;
import com.grayzone.domain.auth.service.AuthService;
import com.grayzone.domain.user.UserTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ResponseDataDto<SignUpResponseDto>> signUp(
    @RequestBody SignUpRequestDto requestDto
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        authService.signUp(requestDto)
      )
    );
  }

  @GetMapping("/terms")
  public ResponseEntity<ResponseDataDto<TermsResponseDto>> getTerms() {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        new TermsResponseDto(
          Stream.of(UserTerm.values()).map(term ->
            new TermsResponseDto.TermResponseDto(term.isRequired(), term.getTitle(), term.getUrl(), term.getCode())
          ).toList())
      )
    );
  }
}
