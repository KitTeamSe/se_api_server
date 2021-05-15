package com.se.apiserver.v1.authority.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthorityGroupAccountMappingErrorCode implements ErrorCode {
    ALREADY_EXIST(400, "AGAC01", "이미 존재하는 그룹-사용자 매핑"),
    NO_SUCH_MAPPING(402, "AGAC02", "존재하지 않는 매핑");;

    private int status;
    private final String code;
    private final String message;

    AuthorityGroupAccountMappingErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
