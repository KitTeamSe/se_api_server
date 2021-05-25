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
    } catch (BusinessException e) {
      resolver.resolveException(request, response, null, e);
    }
    catch (Exception e){
      // 비지니스 익셉션이 아닌 경우
      logger.error(e);
      e.printStackTrace();
      resolver.resolveException(request, response, null, new BusinessException(GlobalErrorCode.UNKNOWN_NON_BUSINESS_ERROR));
    }
  }
}