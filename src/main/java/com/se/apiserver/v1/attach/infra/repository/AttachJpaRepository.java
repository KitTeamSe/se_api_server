package com.se.apiserver.v1.attach.infra.repository;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttachJpaRepository extends JpaRepository<Attach, Long> {

  @Query("select a from Attach a where a.post.postId = :postId")
  List<Attach> findAllByPostId(Long postId);

  @Query("select a from Attach a where a.reply.replyId = :replyId")
  List<Attach> findAllByReplyId(Long replyId);

  @Query("select a from Attach a where a.attachId in (:attachIdList)")
  List<Attach> findAllByAttachId(List<Long> attachIdList);
}
