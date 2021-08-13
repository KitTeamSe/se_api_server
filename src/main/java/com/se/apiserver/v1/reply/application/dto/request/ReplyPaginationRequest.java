package com.se.apiserver.v1.reply.application.dto.request;

import com.se.apiserver.v1.common.application.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

public class ReplyPaginationRequest<T> extends PaginationRequest<T>{
  public ReplyPaginationRequest() {

  }

  public ReplyPaginationRequest(int page, int size, Direction direction,
      String orderBy) {
    super(null, page, size, null, null);
  }
}
