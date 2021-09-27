package com.se.apiserver.v1.common.infra.kafka.config;

import com.se.apiserver.v1.common.infra.logging.SeLogger;
import com.se.apiserver.v1.common.infra.logging.standard.SeStandardLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Configuration
public class ListenableFutureCallbackConfiguration {

  private final SeLogger logger;

  public ListenableFutureCallbackConfiguration() {
    this.logger = new SeStandardLogger();
  }

  @Bean
  public ListenableFutureCallback<SendResult<String, String>> listenableFutureCallback(){
    return new ListenableFutureCallback<>() {
      @Override
      public void onFailure(Throwable ex) {
        logger.error(ex.getClass().getSimpleName(), ex.getMessage());
        logger.debug(ex.getClass().getSimpleName(), ex.getMessage(), ex);
      }

      @Override
      public void onSuccess(SendResult<String, String> result) {
        logger.info(String.valueOf(result.getRecordMetadata().getClass()), result.getRecordMetadata().topic() + ": " + result.getRecordMetadata().offset());
      }
    };
  }

}
