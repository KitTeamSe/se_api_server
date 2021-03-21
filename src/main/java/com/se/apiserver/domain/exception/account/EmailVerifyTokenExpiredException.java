package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class EmailVerifyTokenExpiredException extends BusinessException {

  public EmailVerifyTokenExpiredException() {
    super(AccountErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED);
  }
}
