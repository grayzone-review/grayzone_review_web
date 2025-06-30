package com.grayzone.domain.user.entity;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String nickname;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "main_region_id")
  private LegalDistrict mainRegion;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<InterestedRegion> interestedRegions;

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
