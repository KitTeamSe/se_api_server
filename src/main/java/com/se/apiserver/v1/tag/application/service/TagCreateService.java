package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagCreateService {

    private final TagJpaRepository tagJpaRepository;

    @Transactional
    public Long create(TagCreateDto.Request request) {
        if(tagJpaRepository.findByText(request.getText()).isPresent())
            throw new BusinessException(TagErrorCode.DUPLICATED_TAG);
        Tag tag = new Tag(request.getText());
        tagJpaRepository.save(tag);
        return tag.getTagId();
    }
}
