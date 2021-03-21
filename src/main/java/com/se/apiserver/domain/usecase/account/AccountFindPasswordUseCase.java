package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.exception.account.EmailNotMatchException;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.exception.account.QaNotMatchException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.http.dto.account.AccountFindPasswordDto.Request;
import com.se.apiserver.repository.account.AccountJpaRepository;
import javassist.Loader.Simple;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
@RequiredArgsConstructor
public class AccountFindPasswordUseCase {

  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;
  private final AccountUpdateUseCase accountUpdateUseCase;

  @Value("${spring.mail.username}")
  private String SERVER_EMAIL;

  public boolean findPassword(Request request) {
    Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(() -> new NoSuchAccountException());
    if(!account.getEmail().equals(request.getEmail()))
      throw new EmailNotMatchException();
    if(account.getQuestion().getQuestionId() != request.getQuestionId() || !account.getAnswer().equals(request.getAnswer()))
      throw new QaNotMatchException();

    String randomPassword = RandomString.make();
    accountUpdateUseCase.updatePassword(account, randomPassword);
    accountJpaRepository.save(account);

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject("SE 비밀번호 변경 안내");
    simpleMailMessage.setText("SE 변경된 비밀번호는 " + randomPassword + " 입니다.\n 변경된 비밀번호로 로그인하고 비밀번호를 변경해주세요.");
    simpleMailMessage.setFrom(SERVER_EMAIL);
    simpleMailMessage.setTo(account.getEmail());
    mailSender.send(simpleMailMessage);

    return true;
  }
}
