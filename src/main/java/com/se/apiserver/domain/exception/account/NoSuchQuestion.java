package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class NoSuchQuestion extends BusinessException {
    public NoSuchQuestion() {
        super(AccountErrorCode.NO_SUCH_QUESTION);
    }
}
