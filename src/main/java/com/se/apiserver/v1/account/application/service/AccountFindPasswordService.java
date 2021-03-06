package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountFindPasswordDto.Request;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountFindPasswordService {

  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String SERVER_EMAIL;

  public void findPassword(Request request) {
    Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
    validateEmailMatch(account, request.getEmail());
    validateQuestionMatch(account, request.getQuestionId(), request.getAnswer());
    String randomPassword = RandomString.make();
    account.updatePassword(passwordEncoder.encode(randomPassword));
    accountJpaRepository.save(account);

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject("SE 비밀번호 변경 안내");
    simpleMailMessage.setText("SE 변경된 비밀번호는 " + randomPassword + " 입니다.\n 변경된 비밀번호로 로그인하고 비밀번호를 변경해주세요.");
    simpleMailMessage.setFrom(SERVER_EMAIL);
    simpleMailMessage.setTo(account.getEmail());
    mailSender.send(simpleMailMessage);
  }

  private void validateQuestionMatch(Account account, Long questionId, String answer) {
    if(account.getQuestion().getQuestionId() != questionId || !account.getAnswer().equals(answer))
      throw new BusinessException(AccountErrorCode.QA_NOT_MATCH);
  }

  private void validateEmailMatch(Account account, String email) {
    if(!account.getEmail().equals(email))
      throw new BusinessException(AccountErrorCode.EMAIL_NOT_MATCH);
  }
}
