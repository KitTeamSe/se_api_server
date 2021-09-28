package com.se.apiserver.v1.common.infra.web.interceptor;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.blacklist.application.service.BlacklistDetailService;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class BlacklistInterceptor implements HandlerInterceptor {

  private final BlacklistDetailService blacklistDetailService;
  private final AccountContextService accountContextService;

  public BlacklistInterceptor(BlacklistDetailService blacklistDetailService,
      AccountContextService accountContextService) {
    this.blacklistDetailService = blacklistDetailService;
    this.accountContextService = accountContextService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if(request.getMethod().equals("GET")) {
      return true;
    }

    if(accountContextService.isAnonymous() && blacklistDetailService.isBannedIp(getIp(request))){
      throw new BusinessException(GlobalErrorCode.BANNED_IP);
    }

    if (!accountContextService.isAnonymous() && blacklistDetailService.isBannedAccount(getAccount())) {
      throw new BusinessException(GlobalErrorCode.BANNED_ACCOUNT);
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

  private Account getAccount() {
    return accountContextService.getContextAccount();
  }
}
