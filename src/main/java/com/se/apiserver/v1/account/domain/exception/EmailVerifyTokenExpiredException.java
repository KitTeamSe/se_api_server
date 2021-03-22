package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class EmailVerifyTokenExpiredException extends BusinessException {

  public EmailVerifyTokenExpiredException() {
    super(AccountErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED);
  }
}
