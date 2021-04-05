package com.se.apiserver.security.filter;


import com.se.apiserver.security.provider.JwtTokenResolver;
import com.se.apiserver.v1.blacklist.domain.usecase.BlacklistDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class IpBlacklistFilters extends GenericFilterBean {

  private final BlacklistDetailService blacklistDetailService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String ip = request.getRemoteAddr();
    if(blacklistDetailService.isBaned(ip)){
      res.sendError(HttpStatus.BAD_REQUEST.value(), "차단된 사용자입니다");
      return;
    }
    chain.doFilter(request, response);
  }
}
