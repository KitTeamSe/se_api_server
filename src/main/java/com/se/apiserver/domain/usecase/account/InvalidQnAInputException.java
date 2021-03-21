package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class InvalidQnAInputException extends BusinessException {
    public InvalidQnAInputException() {
        super(AccountErrorCode.QNA_INVALID_INPUT);
    }
}
