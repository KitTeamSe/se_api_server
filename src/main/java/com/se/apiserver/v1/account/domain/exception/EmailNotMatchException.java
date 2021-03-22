package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class EmailNotMatchException extends BusinessException {

  public EmailNotMatchException() {
    super(AccountErrorCode.EMAIL_NOT_MATCH);
  }
}
