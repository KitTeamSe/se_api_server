package com.se.apiserver.v1.common.infra.web.filter;

import com.se.apiserver.v1.common.infra.web.config.CorsHeaderProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class  CorsHeaderFilter {

  private final String MANAGER_SERVER_DOMAIN;
  private final String CLIENT_SERVER_DOMAIN;

  public CorsHeaderFilter(CorsHeaderProperties properties){
    MANAGER_SERVER_DOMAIN = properties.getManagerServerDomain();
    CLIENT_SERVER_DOMAIN = properties.getClientServerDomain();
  }

  @Bean
  public FilterRegistrationBean corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

}
