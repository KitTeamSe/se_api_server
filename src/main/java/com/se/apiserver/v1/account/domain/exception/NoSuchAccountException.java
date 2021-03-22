package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class NoSuchAccountException extends BusinessException {

  public NoSuchAccountException() {
    super(AccountErrorCode.NO_SUCH_ACCOUNT);
  }
}
