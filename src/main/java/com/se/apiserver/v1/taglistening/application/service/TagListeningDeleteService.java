package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.tag.application.error.TagListeningErrorCode;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningDeleteService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountContextService accountContextService;

    @Transactional
    public boolean delete(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TagListeningErrorCode.NO_SUCH_TAG_TAG_LISTENING));

        if(!accountContextService.isOwner(tagListening.getAccount()) && !accountContextService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        tagListeningJpaRepository.delete(tagListening);
        return true;
    }
}
