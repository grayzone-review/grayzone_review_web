package com.grayzone.domain.user.service;

import com.grayzone.domain.user.dto.request.SignUpRequestDto;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.oauth.OAuthUserInfoDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final OAuthUserInfoDispatcher oAuthUserInfoDispatcher;

  public void verifyNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("Nickname already exists");
    }
  }

  public void signUp(SignUpRequestDto requestDto) {

  }
}
