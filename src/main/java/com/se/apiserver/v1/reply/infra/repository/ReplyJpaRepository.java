package com.se.apiserver.v1.reply.infra.repository;

import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

    @Query("select p.replies from Post p where p = :post")
    public List<Reply> findAllBelongPost(Post post);
}
