package com.se.apiserver.v1.common.infra.web.interceptor;

import com.se.apiserver.v1.blacklist.application.service.BlacklistDetailService;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class BlacklistInterceptor implements HandlerInterceptor {

  private final BlacklistDetailService blacklistDetailService;

  public BlacklistInterceptor(BlacklistDetailService blacklistDetailService) {
    this.blacklistDetailService = blacklistDetailService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if(request.getMethod().equals("GET")) {
      return true;
    }

    if(blacklistDetailService.isBaned(getIp(request))){
      throw new BusinessException(GlobalErrorCode.BANNED_IP);
    }
    return true;
  }

  private String getIp(HttpServletRequest request){
    String ip = request.getHeader("x-forwarded-for");
    if(ip == null){
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
