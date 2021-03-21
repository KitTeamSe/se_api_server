package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.account.AccountVerifyStatus;
import com.se.apiserver.domain.entity.account.AccountVerifyToken;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.account.AlreadyVerifiedException;
import com.se.apiserver.domain.exception.account.EmailVerifyTokenExpiredException;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.repository.account.AccountVerifyTokenJpaRepository;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.UUID;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
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

  public void sendVerifyRequestEmailByEmail(String email) {
    sendVerifyRequestEmail(email);
  }

  public void sendVerifyRequestEmailByAccountId(String id) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new NoSuchAccountException());
    sendVerifyRequestEmail(account.getEmail());
  }

  @Transactional
  public void sendVerifyRequestEmail(String email) {

    String token = generateToken();
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(String.format("인증이 완료되었습니다. <a href='%s'>여기</a>를 클릭해주세요\n", (SERVER_DOMAIN + VERIFY_PATH + token)), true);
      helper.setTo(email);
      helper.setSubject("SE 이메일 인증 안내");
      helper.setFrom(SERVER_EMAIL);
      Date now = new Date();
      AccountVerifyToken accountVerifyToken = AccountVerifyToken.builder()
          .token(token)
          .email(email)
          .timeExpire(LocalDateTime.now().plusHours(1))
          .status(AccountVerifyStatus.UNVERIFIED)
          .build();

      accountVerifyTokenJpaRepository.save(accountVerifyToken);

      javaMailSender.send(mimeMessage);
    }catch (Exception e){
    }
  }

  private String generateToken() {
    return UUID.randomUUID().toString();
  }

  public boolean verify(String token) {
    AccountVerifyToken accountVerifyToken = accountVerifyTokenJpaRepository.findFirstByToken(token);
    if(accountVerifyToken.getStatus() == AccountVerifyStatus.VERIFIED)
      throw new AlreadyVerifiedException();

    LocalDateTime now = LocalDateTime.now();
    if(now.isAfter(accountVerifyToken.getTimeExpire()))
      throw new EmailVerifyTokenExpiredException();

    accountVerifyToken.verify();
    accountVerifyTokenJpaRepository.save(accountVerifyToken);
    return true;
  }
}
