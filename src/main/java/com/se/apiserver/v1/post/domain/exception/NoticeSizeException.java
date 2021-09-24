package com.se.apiserver.v1.post.domain.exception;

import com.se.apiserver.v1.common.domain.exception.SeException;
import org.springframework.http.HttpStatus;

public class NoticeSizeException extends SeException {

  public NoticeSizeException(String message) {
    super(HttpStatus.PRECONDITION_FAILED, message);
  }

  public NoticeSizeException(String message,
      Throwable cause) {
    super(HttpStatus.PRECONDITION_FAILED, message, cause);
  }

  public NoticeSizeException(Throwable cause) {
    super(HttpStatus.PRECONDITION_FAILED, cause);
  }
}
