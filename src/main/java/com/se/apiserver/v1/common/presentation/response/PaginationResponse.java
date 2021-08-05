package com.se.apiserver.v1.common.presentation.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaginationResponse<E> extends Response<E> {
  private final int totalData;
  private final int totalPage;
  private final int currentPage;
  private final int perPage;

  public PaginationResponse(HttpStatus status, String message, E data, int totalData, int totalPage,
      int currentPage, int perPage) {
    super(status, message, data);
    this.totalData = totalData;
    this.totalPage = totalPage;
    this.currentPage = currentPage;
    this.perPage = perPage;
  }

  public PaginationResponse(HttpStatus status, String message, int totalData, int totalPage,
      int currentPage, int perPage) {
    super(status, message);
    this.totalData = totalData;
    this.totalPage = totalPage;
    this.currentPage = currentPage;
    this.perPage = perPage;
  }
}
