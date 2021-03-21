package com.se.apiserver.domain.error.account;

import com.se.apiserver.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AccountErrorCode implements ErrorCode {

  NO_SUCH_ACCOUNT(400, "ME01", "존재하지 않는 사용자"),
  PASSWORD_INCORRECT(401, "ME02", "비밀번호 불일치"),
  EMAIL_NOT_MATCH(402, "ME03", "이메일 불일치"),
  QA_NOT_MATCH(403, "ME04", "질문 응답 불일치"),
  EMAIL_VERIFY_TOKEN_EXPIRED(404, "ME05", "이메일 인증 시간 초과");

  private int status;
  private final String code;
  private final String message;

  AccountErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
