package com.se.apiserver.security.service;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.repository.authority.AuthorityJpaRepository;
import lombok.RequiredArgsConstructor;
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

  @Override
  public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
    Account account = accountJpaRepository.findById(Long.parseLong(accountId))
        .orElseThrow(() -> new NoSuchAccountException());

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>(
        authorityJpaRepository.findByAccountId(account.getAccountId()));
    return new User(accountId, account.getPassword(), grantedAuthorities);
  }

  public UserDetails loadDefaultGroupAuthorities(String groupName) throws UsernameNotFoundException {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>(
        authorityJpaRepository.findByAuthorityGroupName(groupName));
    return new User("DEFAULT", "DEFAULT", grantedAuthorities);
  }

  public boolean hasAuthority(String auth) {
    Set<String> authorities = AuthorityUtils
        .authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    return authorities.contains(auth);
  }
}
