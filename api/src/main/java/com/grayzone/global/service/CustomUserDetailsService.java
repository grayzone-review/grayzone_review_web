package com.grayzone.global.service;

import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    long id = Long.parseLong(username);
    return userRepository.findByIdWithMainRegion(id)
      .orElseThrow(() -> new UpException(UpError.USER_NOT_FOUND));
  }
}
