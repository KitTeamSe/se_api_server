package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class EmailNotMatchException extends BusinessException {

  public EmailNotMatchException() {
    super(AccountErrorCode.EMAIL_NOT_MATCH);
  }
}
