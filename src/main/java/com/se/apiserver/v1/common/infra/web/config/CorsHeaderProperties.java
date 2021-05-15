package com.se.apiserver.v1.common.infra.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "se-web-server")
@Component
@Getter
@Setter
public class CorsHeaderProperties {
  private String managerServerDomain;
  private String clientServerDomain;
}
