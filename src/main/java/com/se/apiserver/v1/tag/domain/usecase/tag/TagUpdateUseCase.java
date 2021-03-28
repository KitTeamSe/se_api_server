package com.se.apiserver.v1.tag.domain.usecase.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.dto.tag.TagReadDto;
import com.se.apiserver.v1.tag.infra.dto.tag.TagUpdateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagUpdateUseCase {

    private final TagJpaRepository tagJpaRepository;

    @Transactional
    public TagReadDto.Response update(TagUpdateDto.Request request) {
        if(tagJpaRepository.findByText(request.getText()).isPresent())
            throw new BusinessException(TagErrorCode.DUPLICATED_TAG);
        Tag tag = tagJpaRepository.findById(request.getTagId()).orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        tag.updateText(request.getText());
        tagJpaRepository.save(tag);
        return TagReadDto.Response.fromEntity(tag);
    }
}
