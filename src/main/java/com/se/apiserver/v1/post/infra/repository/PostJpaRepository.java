package com.se.apiserver.v1.post.infra.repository;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
    @Query("select b from Board b where b = :board")
    Page<Post> findAllByBoard(Board board, Pageable pageable);
}
