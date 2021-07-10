package com.se.apiserver.v1.post.infra.repository;

import com.se.apiserver.v1.post.application.dto.PostReadDto;
import com.se.apiserver.v1.post.domain.entity.Post;
import org.springframework.data.domain.Page;

public interface PostQueryRepository {
  Page<Post> search(PostReadDto.SearchRequest searchRequest);
}
