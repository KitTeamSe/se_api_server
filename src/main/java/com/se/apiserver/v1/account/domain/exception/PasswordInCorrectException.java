package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class PasswordInCorrectException extends BusinessException {

  public PasswordInCorrectException() {
    super(AccountErrorCode.PASSWORD_INCORRECT);
  }
}
