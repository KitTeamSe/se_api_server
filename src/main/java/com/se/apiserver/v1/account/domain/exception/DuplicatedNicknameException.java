package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class DuplicatedNicknameException extends BusinessException {
    public DuplicatedNicknameException() {
        super(AccountErrorCode.DUPLICATED_NICKNAME);
    }
}
