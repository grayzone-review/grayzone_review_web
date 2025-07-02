package com.grayzone.domain.user.service;

import com.grayzone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public void verifyNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }
}
