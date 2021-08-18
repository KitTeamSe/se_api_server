package com.se.apiserver.v1.post.application.dto.request;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class AnnouncementPaginationRequest<T> extends PageRequest {

  public AnnouncementPaginationRequest() {

  }

  public AnnouncementPaginationRequest(int page, int size, Direction direction,
      String orderBy) {
    super(page, size, direction);
  }
}
