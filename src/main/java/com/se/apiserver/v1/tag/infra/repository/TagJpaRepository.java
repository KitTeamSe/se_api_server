package com.se.apiserver.v1.tag.infra.repository;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByText(String text);
    List<Tag> findByTextContaining(String text);
}
