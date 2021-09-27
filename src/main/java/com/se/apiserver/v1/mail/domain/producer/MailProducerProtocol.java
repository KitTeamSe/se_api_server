package com.se.apiserver.v1.mail.domain.producer;

import com.se.apiserver.v1.mail.application.dto.MailSendDto;

public interface MailProducerProtocol {
  void sendMail(MailSendDto mailSendDto);
}
