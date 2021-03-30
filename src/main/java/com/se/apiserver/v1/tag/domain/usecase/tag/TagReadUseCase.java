package com.se.apiserver.v1.tag.domain.usecase.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.dto.tag.TagReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagReadUseCase {

    private final TagJpaRepository tagJpaRepository;

    public TagReadDto.Response readById(Long id) {
        Tag tag = tagJpaRepository.findById(id).orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        return TagReadDto.Response.fromEntity(tag);
    }

    public List<TagReadDto.Response> readMatchText(String text) {
        List<Tag> tags = tagJpaRepository.findByTextContaining(text);
        return tags.stream().map(a -> TagReadDto.Response.fromEntity(a)).collect(Collectors.toList());
    }

    public PageImpl readAll(Pageable pageable) {
        Page<Tag> tags = tagJpaRepository.findAll(pageable);
        List<TagReadDto.Response> responseList = tags.stream()
                .map(t -> TagReadDto.Response.fromEntity(t))
                .collect(Collectors.toList());
        return new PageImpl(responseList, tags.getPageable(), tags.getTotalElements());
    }
}