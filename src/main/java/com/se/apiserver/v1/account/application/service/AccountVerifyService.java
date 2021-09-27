package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyStatus;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyToken;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountVerifyTokenJpaRepository;
import com.se.apiserver.v1.mail.application.dto.MailSendDto;
import com.se.apiserver.v1.mail.application.service.MailSendService;
import java.time.LocalDateTime;
import java.util.UUID;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountVerifyService {

  private final AccountJpaRepository accountJpaRepository;
  private final AccountVerifyTokenJpaRepository accountVerifyTokenJpaRepository;
  private final MailSendService mailSendService;

  @Value("${spring.domain}")
  private String SERVER_DOMAIN;
  private final String VERIFY_PATH = "/api/v1/account/verify/";

  public AccountVerifyService(
      AccountJpaRepository accountJpaRepository,
      AccountVerifyTokenJpaRepository accountVerifyTokenJpaRepository,
      MailSendService mailSendService) {
    this.accountJpaRepository = accountJpaRepository;
    this.accountVerifyTokenJpaRepository = accountVerifyTokenJpaRepository;
    this.mailSendService = mailSendService;
  }

  public void sendVerifyRequestEmail(String email) {
    String token = generateToken();
    String text = String.format("인증이 완료되었습니다. <a href='%s'>여기</a>를 클릭해주세요\n", (SERVER_DOMAIN + VERIFY_PATH + token));
    String subject = "SE 이메일 인증 안내";
    AccountVerifyToken accountVerifyToken = new AccountVerifyToken(
        email,
        token,
        LocalDateTime.now().plusHours(1),
        AccountVerifyStatus.UNVERIFIED);

    accountVerifyTokenJpaRepository.save(accountVerifyToken);
    mailSendService.send(new MailSendDto(email, subject, text));
  }

  @Transactional
  public void sendVerifyRequestEmailByEmail(String email) {
    sendVerifyRequestEmail(email);
  }

  @Transactional
  public void sendVerifyRequestEmailByAccountId(String id) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
    sendVerifyRequestEmail(account.getEmail());
  }

  private String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Transactional
  public void verify(String token) {
    AccountVerifyToken accountVerifyToken = accountVerifyTokenJpaRepository.findFirstByToken(token)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_TOKEN));
    if(accountVerifyToken.getStatus() == AccountVerifyStatus.VERIFIED)
      throw new BusinessException(AccountErrorCode.ALREADY_VERIFIED);

    LocalDateTime now = LocalDateTime.now();
    if(now.isAfter(accountVerifyToken.getTimeExpire()))
      throw new BusinessException(AccountErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED);

    accountVerifyToken.verify();
    accountVerifyTokenJpaRepository.save(accountVerifyToken);
  }
}
