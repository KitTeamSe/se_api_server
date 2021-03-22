package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class InvalidQnAInputException extends BusinessException {
    public InvalidQnAInputException() {
        super(AccountErrorCode.QNA_INVALID_INPUT);
    }
}
