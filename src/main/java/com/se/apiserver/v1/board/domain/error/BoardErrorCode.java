package com.se.apiserver.v1.board.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum BoardErrorCode implements ErrorCode {
    NO_SUCH_BOARD(400, "BO01", "존재하지 않는 게시판"),
    DUPLICATED_NAME_KOR(401, "BO2", "게시판 한글명 중복"),
    DUPLICATED_NAME_ENG(402, "BO3", "게시판 영문명 중복"),
    CAN_NOT_ACCESS_POST(403, "PO04", "해당 게시글에 대한 권한이 없습니다.");

    private int status;
    private final String code;
    private final String message;

    BoardErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
