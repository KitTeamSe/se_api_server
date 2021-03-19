package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.account.AccountType;
import com.se.apiserver.domain.entity.account.InformationOpenAgree;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.http.dto.account.AccountCreateDto;
import com.se.apiserver.repository.account.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountCreateUseCase {

  private final AccountJpaRepository accountJpaRepository;

  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Long signUp(AccountCreateDto.Request request) {
    Account account = Account.builder()
        .idString(request.getId())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .nickname(request.getNickname())
        .studentId(request.getStudentId())
        .phoneNumber(request.getPhoneNumber())
        .email(request.getEmail())
        .lastSignInIp("111.111.111.111")
        .informationOpenAgree(InformationOpenAgree.Agree)
        .type(AccountType.ASSISTANT)
        .build();
    System.out.println(account.getIdString());
    accountJpaRepository.save(account);
    return account.getAccountId();
  }

}
