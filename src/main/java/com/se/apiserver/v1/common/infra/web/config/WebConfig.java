package com.se.apiserver.v1.common.infra.web.config;

import com.se.apiserver.v1.blacklist.application.service.BlacklistDetailService;
import com.se.apiserver.v1.common.infra.web.interceptor.BlacklistInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final BlacklistDetailService blacklistDetailService;

  public WebConfig(BlacklistDetailService blacklistDetailService) {
    this.blacklistDetailService = blacklistDetailService;
  }

  @Bean
  public MappedInterceptor blacklistInterceptor() {
    String[] includePatterns = {"/api/v1/post/**", "/api/v1/reply/**"};
    return new MappedInterceptor(includePatterns, new BlacklistInterceptor(blacklistDetailService));
  }
}
