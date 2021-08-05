package com.se.apiserver.v1.post.domain.repository;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.post.application.dto.PostAnnouncementDto;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class PostRepository implements PostRepositoryProtocol{

  private final PostJpaRepository jpa;

  public PostRepository(PostJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Page<Post> findAllByBoardAndIsNoticeEquals(Board board, PostIsNotice postIsNotice, Pageable pageable) {
    return jpa.findAllByBoardAndIsNoticeEquals(board, postIsNotice, pageable);
  }
}
