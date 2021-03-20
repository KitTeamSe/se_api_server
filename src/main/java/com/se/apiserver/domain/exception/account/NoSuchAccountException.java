package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class NoSuchAccountException extends BusinessException {
    public NoSuchAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
