package com.se.apiserver.v1.tag.domain.usecase.taglistening;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.TagListening;
import com.se.apiserver.v1.tag.domain.error.TagListeningErrorCode;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningReadUseCase {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountDetailService accountDetailService;

    public TagListeningReadDto.Response read(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TagListeningErrorCode.NO_SUCH_TAG_TAG_LISTENING));

        if(!accountDetailService.isOwner(tagListening.getAccount()) && !accountDetailService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        return TagListeningReadDto.Response.fromEntity(tagListening);
    }

    public PageImpl readAllByPaging(Pageable pageable){
        Page<TagListening> all = tagListeningJpaRepository.findAll(pageable);
        List<TagListeningReadDto.Response> responseList = all.stream()
                .map(t -> TagListeningReadDto.Response.fromEntity(t))
                .collect(Collectors.toList());

        return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
    }

    public List<TagListeningReadDto.Response> readMy(Long accountId) {
        if(!accountDetailService.isOwner(accountId) && !accountDetailService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        List<TagListening> all = tagListeningJpaRepository.findAllByAccountId(accountId);
        return all.stream().map(a -> TagListeningReadDto.Response.fromEntity(a)).collect(Collectors.toList());
    }
}
