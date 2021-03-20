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

import javax.servlet.http.HttpServletRequest;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountCreateUseCase {

  private final AccountJpaRepository accountJpaRepository;


  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Long signUp(AccountCreateDto.Request request, HttpServletRequest httpServletRequest) {
    Account account = Account.builder()
        .idString(request.getId())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .nickname(request.getNickname())
        .studentId(request.getStudentId())
        .phoneNumber(request.getPhoneNumber())
        .email(request.getEmail())
        .lastSignInIp(httpServletRequest.getRemoteAddr())
        .informationOpenAgree(InformationOpenAgree.DISAGREE)
        .type(AccountType.ASSISTANT)
        .build();
    System.out.println(account.getIdString());
    accountJpaRepository.save(account);
    return account.getAccountId();
  }

}
