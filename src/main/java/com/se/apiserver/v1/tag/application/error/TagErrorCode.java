package com.se.apiserver.v1.tag.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum TagErrorCode implements ErrorCode {
    NO_SUCH_TAG(400,"TG01","존재하지 않는 태그"),
    DUPLICATED_TAG(401,"TG02","태그명 중복"),
    ANONYMOUS_CAN_NOT_TAG(400, "TG03", "익명 사용자는 태그를 달 수 없습니다"),
    TO_SHORT_LENGTH(400, "TG04", "검색어는 최소 두 글자 이상입니다.");

    private int status;
    private final String code;
    private final String message;

    TagErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
