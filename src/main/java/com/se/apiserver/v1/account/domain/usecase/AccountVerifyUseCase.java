package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyStatus;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyToken;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountVerifyTokenJpaRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.mail.internet.MimeMessage;

import com.se.apiserver.v1.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountVerifyUseCase {

  private final AccountJpaRepository accountJpaRepository;

  private final AccountVerifyTokenJpaRepository accountVerifyTokenJpaRepository;

  @Value("${spring.mail.username}")
  private String SERVER_EMAIL;

  @Value("${spring.domain}")
  private String SERVER_DOMAIN;

  private final String VERIFY_PATH = "/api/v1/account/verify/";

  private final JavaMailSender javaMailSender;

  private final Long VERIFY_TOKEN_LIFE_TIME = 1000L * 60 * 60;

  @Transactional
  boolean sendVerifyRequestEmail(String email) {

    String token = generateToken();
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(String.format("인증이 완료되었습니다. <a href='%s'>여기</a>를 클릭해주세요\n", (SERVER_DOMAIN + VERIFY_PATH + token)), true);
      helper.setTo(email);
      helper.setSubject("SE 이메일 인증 안내");
      helper.setFrom(SERVER_EMAIL);
      AccountVerifyToken accountVerifyToken = AccountVerifyToken.builder()
              .token(token)
              .email(email)
              .timeExpire(LocalDateTime.now().plusHours(1))
              .status(AccountVerifyStatus.UNVERIFIED)
              .build();

      accountVerifyTokenJpaRepository.save(accountVerifyToken);

      javaMailSender.send(mimeMessage);
      return true;
    }catch (Exception e){
      return false;
    }
  }


  public boolean sendVerifyRequestEmailByEmail(String email) {
    return sendVerifyRequestEmail(email);
  }

  public boolean sendVerifyRequestEmailByAccountId(String id) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
    return sendVerifyRequestEmail(account.getEmail());
  }



  private String generateToken() {
    return UUID.randomUUID().toString();
  }

  public boolean verify(String token) {
    AccountVerifyToken accountVerifyToken = accountVerifyTokenJpaRepository.findFirstByToken(token);
    if(accountVerifyToken.getStatus() == AccountVerifyStatus.VERIFIED)
      throw new BusinessException(AccountErrorCode.ALREADY_VERIFIED);

    LocalDateTime now = LocalDateTime.now();
    if(now.isAfter(accountVerifyToken.getTimeExpire()))
      throw new BusinessException(AccountErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED);

    accountVerifyToken.verify();
    accountVerifyTokenJpaRepository.save(accountVerifyToken);
    return true;
  }
}
