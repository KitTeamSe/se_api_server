package com.se.apiserver.v1.tag.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum TagErrorCode implements ErrorCode {
    NO_SUCH_TAG(400,"TG01","존재하지 않는 태그"),
    DUPLICATED_TAG(401,"TG02","태그명 중복");
    private int status;
    private final String code;
    private final String message;

    TagErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
