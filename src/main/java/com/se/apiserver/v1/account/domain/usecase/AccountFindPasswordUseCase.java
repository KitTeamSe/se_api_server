package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountFindPasswordDto.Request;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
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
    Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
    if(!account.getEmail().equals(request.getEmail()))
      throw new BusinessException(AccountErrorCode.EMAIL_NOT_MATCH);
    if(account.getQuestion().getQuestionId() != request.getQuestionId() || !account.getAnswer().equals(request.getAnswer()))
      throw new BusinessException(AccountErrorCode.QA_NOT_MATCH);

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
