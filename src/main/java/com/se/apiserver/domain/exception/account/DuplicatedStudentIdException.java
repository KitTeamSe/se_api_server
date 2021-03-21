package com.se.apiserver.domain.exception.account;

import com.se.apiserver.domain.error.ErrorCode;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.BusinessException;

public class DuplicatedStudentIdException extends BusinessException {
    public DuplicatedStudentIdException() {
        super(AccountErrorCode.DUPLICATED_STUDENT_ID);
    }
}
