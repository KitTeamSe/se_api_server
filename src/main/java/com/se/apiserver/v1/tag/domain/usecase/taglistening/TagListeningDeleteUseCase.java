package com.se.apiserver.v1.tag.domain.usecase.taglistening;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.TagListening;
import com.se.apiserver.v1.tag.domain.error.TagListeningErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.tag.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningDeleteUseCase {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountDetailService accountDetailService;

    public boolean delete(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TagListeningErrorCode.NO_SUCH_TAG_TAG_LISTENING));

        if(!accountDetailService.isOwner(tagListening.getAccount()) && !accountDetailService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        tagListeningJpaRepository.delete(tagListening);
        return true;
    }
}
