package com.se.apiserver.v1.common.infra.security.filter;


import com.se.apiserver.v1.blacklist.application.service.BlacklistDetailService;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    HttpServletRequest re = (HttpServletRequest) request;
    String ip = re.getHeader("x-forwarded-for");
    ip = ip != null ? ip : re.getRemoteAddr();

    if(blacklistDetailService.isBaned(ip)){
      throw new BusinessException(GlobalErrorCode.BANNED_IP);
    }
    chain.doFilter(request, response);
  }
}
