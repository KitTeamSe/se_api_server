package com.se.apiserver.v1.reply.infra.repository;

import com.se.apiserver.v1.reply.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

}
