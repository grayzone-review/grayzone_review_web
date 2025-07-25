package com.grayzone.domain.user.entity;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.global.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  @Column(unique = true, nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private OAuthProvider oAuthProvider;

  private String oAuthId;
  private String oAuthRefreshToken;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "main_region_id")
  private LegalDistrict mainRegion;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<InterestedRegion> interestedRegions = new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FollowCompany> followCompanies;

  private boolean agreedServiceUse;
  private boolean agreedPrivacy;
  private boolean agreedLocation;

  @Builder
  public User(
    String email,
    String nickname,
    OAuthProvider oAuthProvider,
    String oAuthId,
    LegalDistrict mainRegion,
    boolean agreedServiceUse,
    boolean agreedPrivacy,
    boolean agreedLocation
  ) {
    this.email = email;
    this.nickname = nickname;
    this.oAuthProvider = oAuthProvider;
    this.oAuthId = oAuthId;
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

  public String getMainRegionAddress() {
    return mainRegion.getAddress();
  }

  public void setInterestedRegions(List<InterestedRegion> interestedRegions) {
    this.interestedRegions.clear();
    this.interestedRegions.addAll(interestedRegions);
  }

  public boolean requiresAppleRefreshToken() {
    return this.oAuthProvider == OAuthProvider.APPLE && this.oAuthRefreshToken == null;
  }
}
