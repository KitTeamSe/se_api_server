package com.se.apiserver.v1.mail.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailSendDto {
  @Email
  private String rcpt;
  @Email
  private String from;
  @NotBlank
  private String subject;
  @NotBlank
  private String text;

  public MailSendDto(String rcpt, String subject, String text) {
    this.rcpt = rcpt;
    this.subject = subject;
    this.text = text;
  }

  public void setFrom(String from){
    this.from = from;
  }
}
