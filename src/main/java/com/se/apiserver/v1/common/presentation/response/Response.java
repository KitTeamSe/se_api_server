package com.se.apiserver.v1.common.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<E> {

  private HttpStatus status;
  private final String message;
  private E data;

  public Response(HttpStatus status, String message, E data) {
    this(status, message);
    this.data = data;
  }

  public Response(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
