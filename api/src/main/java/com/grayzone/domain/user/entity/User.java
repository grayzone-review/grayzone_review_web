package com.grayzone.domain.user.entity;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.global.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private OAuthProvider oAuthProvider;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "main_region_id")
  private LegalDistrict mainRegion;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<InterestedRegion> interestedRegions;

  private boolean agreedServiceUse;
  private boolean agreedPrivacy;
  private boolean agreedLocation;

  @Builder
  public User(
    String email,
    String nickname,
    OAuthProvider oAuthProvider,
    LegalDistrict mainRegion,
    boolean agreedServiceUse,
    boolean agreedPrivacy,
    boolean agreedLocation
  ) {
    this.email = email;
    this.nickname = nickname;
    this.oAuthProvider = oAuthProvider;
    this.mainRegion = mainRegion;
    this.agreedServiceUse = agreedServiceUse;
    this.agreedPrivacy = agreedPrivacy;
    this.agreedLocation = agreedLocation;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return getEmail();
  }
}
