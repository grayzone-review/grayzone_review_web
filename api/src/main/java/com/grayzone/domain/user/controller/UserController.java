package com.grayzone.domain.user.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.user.UserTerm;
import com.grayzone.domain.user.dto.request.VerifyNicknameDuplicateRequestDto;
import com.grayzone.domain.user.dto.response.TermsResponseDto;
import com.grayzone.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

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
        new TermsResponseDto(
          Stream.of(UserTerm.values()).map(term ->
            new TermsResponseDto.TermResponseDto(term.isRequired(), term.getTitle(), term.getUrl(), term.getCode())
          ).toList())
      )
    );
  }
}
