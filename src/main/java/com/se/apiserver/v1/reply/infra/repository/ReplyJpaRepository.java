package com.se.apiserver.v1.reply.infra.repository;

import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.post = :post and r.parent is null order by r.replyId")
    public List<Reply> findAllBelongPost(Post post);
}
