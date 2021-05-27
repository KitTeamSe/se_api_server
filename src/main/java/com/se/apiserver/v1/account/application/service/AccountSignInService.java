package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountSignInDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.infra.security.provider.JwtTokenResolver;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountSignInService {

  private final JwtTokenResolver jwtTokenResolver;
  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public AccountSignInDto.Response signIn(String id, String password, String ip) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
    if (!passwordEncoder.matches(password, account.getPassword())) {
        throw new BusinessException(AccountErrorCode.PASSWORD_INCORRECT);
    }

    account.updateLastSignIp(ip);
    accountJpaRepository.save(account);

    String token = jwtTokenResolver.createToken(String.valueOf(account.getAccountId()));
    return new AccountSignInDto.Response(token);
  }
}
