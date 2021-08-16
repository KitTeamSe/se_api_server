package com.se.apiserver.v1.reply.application.dto.request;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
public class ReplyPaginationRequest{
  private int page;
  private int size;

  public ReplyPaginationRequest() {

  }

  public ReplyPaginationRequest(int page, int size) {
    this.page = page;
    this.size= size;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public PageRequest of() {
    return PageRequest.of(page, size);
  }
}
