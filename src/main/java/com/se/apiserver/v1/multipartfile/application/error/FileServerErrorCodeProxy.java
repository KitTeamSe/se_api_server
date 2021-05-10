package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import java.beans.ConstructorProperties;
import lombok.Getter;

// 파일서버로부터 받은 에러 코드
@Getter
public class FileServerErrorCodeProxy implements ErrorCode {

  private final String code;
  private final String message;
  private int status;

  @ConstructorProperties({"status", "code", "message"})
  FileServerErrorCodeProxy(final int status, final String code, final String message) {
    this.status = status;
    this.message = "FS-" + message;
    this.code = "FS-" + code;
  }
}
