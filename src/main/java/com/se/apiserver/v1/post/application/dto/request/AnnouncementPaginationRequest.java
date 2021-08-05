package com.se.apiserver.v1.post.application.dto.request;

import com.se.apiserver.v1.common.application.PaginationRequest;
import org.springframework.data.domain.Sort.Direction;

public class AnnouncementPaginationRequest<T> extends PaginationRequest<T> {

  public AnnouncementPaginationRequest() {

  }

  public AnnouncementPaginationRequest(int page, int size, Direction direction,
      String orderBy) {
    super(null, page, size, direction, orderBy);
  }
}
