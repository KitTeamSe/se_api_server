package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.tag.application.error.TagListeningErrorCode;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningReadDto;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningReadService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountContextService accountContextService;

    public TagListeningReadDto.Response read(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TagListeningErrorCode.NO_SUCH_TAG_TAG_LISTENING));

        if(!accountContextService.isOwner(tagListening.getAccount()) && !accountContextService.hasAuthority("TAG_MANAGE"))
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
        if(!accountContextService.isOwner(accountId) && !accountContextService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        List<TagListening> all = tagListeningJpaRepository.findAllByAccountId(accountId);
        return all.stream().map(a -> TagListeningReadDto.Response.fromEntity(a)).collect(Collectors.toList());
    }
}
