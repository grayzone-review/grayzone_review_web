package com.grayzone.domain.user.repository;

import com.grayzone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByNickname(String nickname);

  Optional<User> findByEmail(String email);

  @Query(value = """
    SELECT u FROM User u 
    JOIN FETCH u.mainRegion
    JOIN FETCH u.interestedRegions
    WHERE u.id = :userId
    """)
  Optional<User> findByIdWithRegion(@Param("userId") Long userId);
}
