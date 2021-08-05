package com.se.apiserver.v1.post.domain.repository;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryProtocol {
   Page<Post> findAllByBoardAndIsNoticeEquals(Board board, PostIsNotice postIsNotice, Pageable pageable);
}
