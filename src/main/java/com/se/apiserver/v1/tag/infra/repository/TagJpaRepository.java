package com.se.apiserver.v1.tag.infra.repository;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByText(String text);
    List<Tag> findByTextContainingIgnoreCase(String text);

    @Query(value = "SELECT * FROM tag WHERE MATCH(text) AGAINST(?1 in boolean mode)", nativeQuery = true)
    List<Tag> findAllByText(String text);
}
