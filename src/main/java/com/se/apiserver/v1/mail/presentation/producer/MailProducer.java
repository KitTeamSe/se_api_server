package com.se.apiserver.v1.mail.presentation.producer;

import com.google.gson.Gson;
import com.se.apiserver.v1.mail.application.dto.MailSendDto;
import com.se.apiserver.v1.mail.domain.producer.MailProducerProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class MailProducer implements MailProducerProtocol {

  @Value("${kafka.topic.mail.name}")
  private String topic;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Gson gson;
  private final ListenableFutureCallback<SendResult<String, String>> listenableFutureCallback;

  public MailProducer(
      KafkaTemplate<String, String> kafkaTemplate,
      ListenableFutureCallback<SendResult<String, String>> listenableFutureCallback) {
    this.kafkaTemplate = kafkaTemplate;
    this.listenableFutureCallback = listenableFutureCallback;
    gson = new Gson();
  }

  public void sendMail(MailSendDto mailSendDto){
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, gson.toJson(mailSendDto));
    future.addCallback(listenableFutureCallback);
  }
}
