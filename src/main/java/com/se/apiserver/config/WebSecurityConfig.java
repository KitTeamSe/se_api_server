package com.se.apiserver.config;

import com.se.apiserver.security.filter.JwtAuthenticationFilters;
import com.se.apiserver.security.provider.JwtTokenResolver;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenResolver jwtTokenResolver;

  private final AccountDetailService accountDetailService;

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/api/v1/signup", "/api/v1/signin").permitAll()
        .antMatchers("/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(new JwtAuthenticationFilters(jwtTokenResolver), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs", "...");
  }
}

