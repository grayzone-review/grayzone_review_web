package com.grayzone.domain.auth.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.auth.dto.request.LoginRequestDto;
import com.grayzone.domain.auth.dto.request.LogoutRequestDto;
import com.grayzone.domain.auth.dto.request.ReissueRequestDto;
import com.grayzone.domain.auth.dto.request.SignUpRequestDto;
import com.grayzone.domain.auth.dto.response.LoginResponseDto;
import com.grayzone.domain.auth.dto.response.ReissueResponseDto;
import com.grayzone.domain.auth.dto.response.TermsResponseDto;
import com.grayzone.domain.auth.service.AuthService;
import com.grayzone.domain.user.UserTerm;
import jakarta.validation.Valid;
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
  public ResponseEntity<ResponseDataDto<Void>> signUp(
    @Valid @RequestBody SignUpRequestDto requestDto
  ) {
    authService.signUp(requestDto);

    return ResponseEntity.ok(
      ResponseDataDto.from(
        null
      )
    );
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDataDto<LoginResponseDto>> login(
    @Valid @RequestBody LoginRequestDto requestDto
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        authService.login(requestDto)
      )
    );
  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseDataDto<Void>> logout(
    @RequestBody LogoutRequestDto requestDto
  ) {
    authService.logout(requestDto.getRefreshToken());
    
    return ResponseEntity.ok(
      ResponseDataDto.from(
        null
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

  @PostMapping("/reissue")
  public ResponseEntity<ResponseDataDto<ReissueResponseDto>> reissue(
    @Valid @RequestBody ReissueRequestDto requestDto
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        authService.reissue(requestDto.getRefreshToken())
      )
    );
  }
}
