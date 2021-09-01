package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagListeningDeleteService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountContextService accountContextService;

    public TagListeningDeleteService(
        TagListeningJpaRepository tagListeningJpaRepository,
        AccountContextService accountContextService) {
        this.tagListeningJpaRepository = tagListeningJpaRepository;
        this.accountContextService = accountContextService;
    }

    @Transactional
    public boolean delete(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 수신 태그"));

        if(!accountContextService.isOwner(tagListening.getAccount()) && !accountContextService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        tagListeningJpaRepository.delete(tagListening);
        return true;
    }
}
