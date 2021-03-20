package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class PasswordInCorrectException extends BusinessException {
    public PasswordInCorrectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
