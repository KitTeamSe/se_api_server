package com.se.apiserver.v1.common.infra.security.filter;

import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
    try {
      filterChain.doFilter(request, response);
    }
    catch (Exception e){
      if(e instanceof BusinessException){
        resolveException(request, response, (BusinessException) e);
      }
      else{
        logger.error(e);
        e.printStackTrace();
        resolveException(request, response, new BusinessException(GlobalErrorCode.UNKNOWN_NON_BUSINESS_ERROR));
      }
    }
  }

  private void resolveException(HttpServletRequest request, HttpServletResponse response, BusinessException e){
    setHeaderForCors(response);
    resolver.resolveException(request, response, null, e);
  }

  private void setHeaderForCors(HttpServletResponse response){
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,observe");
  }
}