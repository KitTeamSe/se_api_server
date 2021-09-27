package com.se.apiserver.v1.mail.application.service;

import com.se.apiserver.v1.mail.application.dto.MailSendDto;
import com.se.apiserver.v1.mail.domain.producer.MailProducerProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailSendService {

  private final MailProducerProtocol mailProducer;

  @Value("${spring.mail.from}")
  private String SERVER_EMAIL;

  public MailSendService(MailProducerProtocol mailProducer) {
    this.mailProducer = mailProducer;
  }

  public void send(MailSendDto mailSendDto) {
    mailSendDto.setFrom(SERVER_EMAIL);
    mailProducer.sendMail(mailSendDto);
  }
}
