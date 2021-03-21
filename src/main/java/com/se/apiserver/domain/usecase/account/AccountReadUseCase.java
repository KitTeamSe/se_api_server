package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.http.dto.common.PageRequest;
import com.se.apiserver.repository.account.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import java.util.Optional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;

@UseCase
@RequiredArgsConstructor
public class AccountReadUseCase {

  private final AccountJpaRepository accountJpaRepository;


  public Account read(Long accountId) {
    Optional<Account> member = accountJpaRepository.findById(accountId);
    if (!member.isPresent()) {
      throw new NoSuchAccountException();
    }
    return member.get();
  }

  public Page<Account> readAll(PageRequest pageRequest) {
    return accountJpaRepository.findAll(pageRequest.of());
  }

  public Account readByEmail(String email) {
    Account account = accountJpaRepository.findByEmail(email).orElseThrow(() -> new NoSuchAccountException());
    return account;
  }


}

