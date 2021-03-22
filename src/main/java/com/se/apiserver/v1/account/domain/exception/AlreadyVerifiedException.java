package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class AlreadyVerifiedException extends BusinessException {
    public AlreadyVerifiedException() {
        super(AccountErrorCode.ALREADY_VERIFIED);
    }
}
