package com.se.apiserver.v1.authority.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthorityGroupErrorCode implements ErrorCode {
    NO_SUCH_AUTHORITY_GROUP(400, "AGO1", "권한 그룹이 존재하지 않음"),
    DUPLICATED_GROUP_NAME(400, "AGO2", "권한 그룹명 중복"),
    DEFAULT_GROUP_ONLY_ONE(400, "AGO3", "기본 사용자 그룹은 오직 하나만 존재 가능"),
    ANONYMOUS_GROUP_ONLY_ONE(400, "AGO4", "익명 사용자 그룹은 오직 하나만 존재 가능"),
    CAN_NOT_DELETE_DEFAULT_GROUP(400, "AGO5", "기본 사용자 그룹은 삭제 불가능"),
    CAN_NOT_DELETE_ANONYMOUS_GROUP(400, "AGO6", "익명 사용자 그룹은 삭제 불가능"),
    CAN_NOT_DELETE_SYSTEM_GROUP(400, "AGO7", "시스템 사용자 그룹은 삭제 불가능"),
    NO_DEFAULT_AUTHORITY_GROUP(400, "AGO8", "기본 권한 그룹이 존재하지 않음");

    private int status;
    private final String code;
    private final String message;

    AuthorityGroupErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
