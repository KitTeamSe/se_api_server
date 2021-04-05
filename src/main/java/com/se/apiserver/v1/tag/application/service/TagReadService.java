package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.application.dto.TagReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagReadService {

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
