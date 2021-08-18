package com.se.apiserver.v1.reply.application.dto.request;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

public class ReplyPaginationRequest extends PageRequest {
  public ReplyPaginationRequest() {

  }

  public ReplyPaginationRequest(int page, int size, Direction direction) {
    super(page, size, direction);
  }
}
