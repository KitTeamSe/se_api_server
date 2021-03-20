package com.se.apiserver.domain.exception;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.GlobalErrorCode;

public abstract class BusinessException extends RuntimeException{

    private  ErrorCode errorCode;
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
