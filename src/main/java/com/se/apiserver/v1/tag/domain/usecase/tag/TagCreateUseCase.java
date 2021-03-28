package com.se.apiserver.v1.tag.domain.usecase.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.dto.tag.TagCreateDto;
import com.se.apiserver.v1.tag.infra.dto.tag.TagReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagCreateUseCase {

    private final TagJpaRepository tagJpaRepository;

    @Transactional
    public TagReadDto.Response create(TagCreateDto.Request request) {
        if(tagJpaRepository.findByText(request.getText()).isPresent())
            throw new BusinessException(TagErrorCode.DUPLICATED_TAG);

        Tag tag = Tag.builder()
                .text(request.getText())
                .build();
        tagJpaRepository.save(tag);
        return TagReadDto.Response.fromEntity(tag);
    }
}
