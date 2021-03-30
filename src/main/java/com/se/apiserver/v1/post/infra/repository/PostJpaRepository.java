package com.se.apiserver.v1.post.infra.repository;

import com.se.apiserver.v1.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
}
