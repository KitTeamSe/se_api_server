package com.se.apiserver.v1.tag.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum  TagListeningErrorCode implements ErrorCode {
    NO_SUCH_TAG_TAG_LISTENING(400,"TL01","존재하지 않는 수신 태그"),
    DUPLICATED(401,"TL02","중복된 내용 삽입");
    private int status;
    private final String code;
    private final String message;

    TagListeningErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
