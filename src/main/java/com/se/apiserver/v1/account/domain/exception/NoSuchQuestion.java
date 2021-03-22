package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class NoSuchQuestion extends BusinessException {
    public NoSuchQuestion() {
        super(AccountErrorCode.NO_SUCH_QUESTION);
    }
}
