package com.se.apiserver.v1.taglistening.infra.repository;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagListeningJpaRepository extends JpaRepository<TagListening, Long> {

    @Query("select tl " +
            "from TagListening tl " +
            "where tl.account.accountId = :accountId and tl.tag.tagId = :tagId")
    Optional<TagListening> findByAccountIdAndTagId(Long accountId, Long tagId);

    @Query("select tl " +
            "from TagListening tl " +
            "where tl.account.accountId = :accountId")
    List<TagListening> findAllByAccountId(Long accountId);

    @Query("select tl " +
            "from TagListening tl " +
            "where tl.tag.tagId = :tagId")
    List<TagListening> findAllByTagId(Long tagId);

    void deleteAllByTag(Tag tag);

   // List<TagListening> findTagListeningsByTag_TagId(List<Long> tagIdList);
}
