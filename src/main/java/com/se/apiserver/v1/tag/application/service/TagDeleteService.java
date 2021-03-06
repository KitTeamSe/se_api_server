package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagDeleteService {

    private final TagJpaRepository tagJpaRepository;

    @Transactional
    public boolean delete(Long id) {
        Tag tag = tagJpaRepository.findById(id).orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        tagJpaRepository.delete(tag);
        return true;
    }
}
