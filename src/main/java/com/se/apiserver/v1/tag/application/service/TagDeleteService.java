package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagDeleteService {

    private final TagJpaRepository tagJpaRepository;
    private final TagListeningJpaRepository tagListeningJpaRepository;

    public TagDeleteService(
        TagJpaRepository tagJpaRepository,
        TagListeningJpaRepository tagListeningJpaRepository) {
        this.tagJpaRepository = tagJpaRepository;
        this.tagListeningJpaRepository = tagListeningJpaRepository;
    }

    @Transactional
    public boolean delete(Long id) {
        Tag tag = tagJpaRepository.findById(id).orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        tagListeningJpaRepository.deleteAllByTag(tag);
        tagJpaRepository.delete(tag);
        return true;
    }
}
