package com.se.apiserver.v1.common.infra.web.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class  CorsHeaderFilter {

  @Value("${se-api-server.api-server-domain}")
  private String apiServerDomain;

  @Value("${se-api-server.manager-server-domain}")
  private String managerServerDomain;

  @Value("${se-api-server.client-server-domain}")
  private String clientServerDomain;

  @Bean
  public FilterRegistrationBean corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedOrigin(apiServerDomain);
    config.addAllowedOrigin(managerServerDomain);
    config.addAllowedOrigin(clientServerDomain);
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

}
