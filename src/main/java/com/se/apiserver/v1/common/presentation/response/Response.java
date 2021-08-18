package com.se.apiserver.v1.common.presentation.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class Response<E> {

  private HttpStatus status;
  private String message;
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
