package com.se.apiserver.v1.account.domain.exception;

import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.exception.BusinessException;

public class DuplicatedStudentIdException extends BusinessException {
    public DuplicatedStudentIdException() {
        super(AccountErrorCode.DUPLICATED_STUDENT_ID);
    }
}
