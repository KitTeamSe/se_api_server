package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class AlreadyVerifiedException extends BusinessException {
    public AlreadyVerifiedException() {
        super(AccountErrorCode.ALREADY_VERIFIED);
    }
}
