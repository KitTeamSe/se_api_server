package com.se.apiserver.v1.board.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum BoardErrorCode implements ErrorCode {
    NO_SUCH_BOARD(400, "BO01", "존재하지 않는 사용자"),
    DUPLICATED_NAME_KOR(401, "BO2", "게시판 한글명 중복"),
    DUPLICATED_NAME_ENG(402, "BO3", "게시판 영문명 중복");

    private int status;
    private final String code;
    private final String message;

    BoardErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
