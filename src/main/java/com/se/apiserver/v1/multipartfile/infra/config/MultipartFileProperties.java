package com.se.apiserver.v1.multipartfile.infra.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "se-file-server")
@Component
@Getter
@Setter
public class MultipartFileProperties {
  private String domain;
  private long maxFileSize;
}
