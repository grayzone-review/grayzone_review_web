package com.grayzone.domain.user.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.user.dto.request.VerifyNicknameDuplicateRequestDto;
import com.grayzone.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
      .status(HttpStatus.OK)
      .body(ResponseDataDto.from(null));
  }
}
