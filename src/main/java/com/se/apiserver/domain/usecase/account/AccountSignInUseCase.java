package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.exception.account.PasswordInCorrectException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.security.provider.JwtTokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
@RequiredArgsConstructor
public class AccountSignInUseCase {

  private final JwtTokenResolver jwtTokenResolver;
  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;

  public String signIn(String id, String password) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new NoSuchAccountException());

      if (!passwordEncoder.matches(password, account.getPassword())) {
          throw new PasswordInCorrectException();
      }

    String token = jwtTokenResolver.createToken(String.valueOf(account.getAccountId()));
    return token;
  }
}
