package com.se.apiserver.v1.attach.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AttachErrorCode implements ErrorCode {
    NO_SUCH_ATTACH(400, "PO01", "존재하지 않는 첨부파일"),
    INVALID_INPUT(401, "PO02", "입력 값이 올바르지 않음");
    private int status;
    private final String code;
    private final String message;

    AttachErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
