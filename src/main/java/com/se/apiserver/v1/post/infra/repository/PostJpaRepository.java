package com.se.apiserver.v1.post.infra.repository;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
    @Query("select p from Post p where p.board = :board and p.isNotice = :postIsNotice and p.postIsDeleted = com.se.apiserver.v1.post.domain.entity.PostIsDeleted.NORMAL")
    Page<Post> findAllByBoardAndIsNotice(Pageable pageable, Board board, PostIsNotice postIsNotice);

    @Query("select p from Post p where p.board = :board")
    List<Post> findAllByBoard(Board board);

    Page<Post> findAllByBoardAndIsNoticeEquals(Board board, PostIsNotice postIsNotice, Pageable pageable);
}
