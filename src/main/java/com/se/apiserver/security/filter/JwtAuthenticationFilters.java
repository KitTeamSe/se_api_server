package com.se.apiserver.security.filter;


import com.se.apiserver.domain.entity.authority.Authority;
import com.se.apiserver.security.provider.JwtTokenResolver;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilters extends GenericFilterBean {

  private final JwtTokenResolver jwtTokenResolver;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String token = jwtTokenResolver.resolveToken((HttpServletRequest) request);
    Authentication auth;
      if (token != null && jwtTokenResolver.validateToken(token)) {
          auth = jwtTokenResolver.getAuthentication(token);
      } else {
          auth = jwtTokenResolver.getDefaultAuthentication();
      }

    SecurityContextHolder.getContext().setAuthentication(auth);
    chain.doFilter(request, response);
  }
}
