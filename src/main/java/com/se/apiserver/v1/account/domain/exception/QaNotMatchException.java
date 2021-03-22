package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class QaNotMatchException extends BusinessException {

  public QaNotMatchException() {
    super(AccountErrorCode.QA_NOT_MATCH);
  }
}
