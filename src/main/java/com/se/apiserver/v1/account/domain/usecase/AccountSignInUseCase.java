package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountSignInDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.security.provider.JwtTokenResolver;
import com.se.apiserver.v1.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountSignInUseCase {

  private final JwtTokenResolver jwtTokenResolver;
  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;

  public AccountSignInDto.Response signIn(String id, String password) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

      if (!passwordEncoder.matches(password, account.getPassword())) {
          throw new BusinessException(AccountErrorCode.PASSWORD_INCORRECT);
      }

    String token = jwtTokenResolver.createToken(String.valueOf(account.getAccountId()));
    return new AccountSignInDto.Response(token);
  }
}
