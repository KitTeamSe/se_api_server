package com.se.apiserver.domain.entity.account;

import com.se.apiserver.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class Account extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountId;

  @Size(min = 5, max = 20)
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String password;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String nickname;

  @Size(min = 8, max = 20)
  @Column(nullable = false)
  private String studentId;

  @Column(nullable = false)
  private AccountType type;

  @Size(min = 10, max = 20)
  @Column
  private String phoneNumber;

  @Size(min = 4, max = 40)
  @Column(nullable = false)
  private String email;

  @Column(length = 20)
  @Size(min = 4, max = 20)
  private String lastSignInIp;

  @Size(min = 2, max = 10)
  @Column(nullable = false)
  private InformationOpenAgree informationOpenAgree;

  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  protected Account() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return String.valueOf(this.getAccountId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
