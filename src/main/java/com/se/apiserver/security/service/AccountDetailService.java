package com.se.apiserver.security.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

  private final AccountJpaRepository accountJpaRepository;
  private final AuthorityJpaRepository authorityJpaRepository;

  @Value("${spring.security.anonymous.id}")
  private String ANONYMOUS_ID;

  @Value("${spring.security.anonymous.pw}")
  private String ANONYMOUS_PW;

  @Override
  public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
    Account account = accountJpaRepository.findById(Long.parseLong(accountId))
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>(
        authorityJpaRepository.findByAccountId(account.getAccountId()));
    return new User(account.getIdString(), account.getPassword(), grantedAuthorities);
  }

  public UserDetails loadDefaultGroupAuthorities(String groupName) throws UsernameNotFoundException {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>(
        authorityJpaRepository.findByAuthorityGroupName(groupName));
    return new User(ANONYMOUS_ID, ANONYMOUS_PW, grantedAuthorities);
  }

  public boolean hasAuthority(String auth) {
    Set<String> authorities = AuthorityUtils
        .authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    return authorities.contains(auth);
  }

  public boolean isOwner(Account account) {
    if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getName() == null)
      throw new AccessDeniedException("비정상적인 접근");

    String id = SecurityContextHolder.getContext().getAuthentication().getName();

    if(id.equals(ANONYMOUS_ID))
      return false;
    if(!id.equals(account.getIdString()))
      return false;
    return true;
  }
}
