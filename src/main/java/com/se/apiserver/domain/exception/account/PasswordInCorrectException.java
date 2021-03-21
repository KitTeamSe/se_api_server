package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class PasswordInCorrectException extends BusinessException {

  public PasswordInCorrectException() {
    super(AccountErrorCode.PASSWORD_INCORRECT);
  }
}
