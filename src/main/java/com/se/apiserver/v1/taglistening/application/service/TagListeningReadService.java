package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningReadDto;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningReadDto.Response;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagListeningReadService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final AccountContextService accountContextService;

    public TagListeningReadService(
        TagListeningJpaRepository tagListeningJpaRepository,
        AccountContextService accountContextService) {
        this.tagListeningJpaRepository = tagListeningJpaRepository;
        this.accountContextService = accountContextService;
    }

    public TagListeningReadDto.Response read(Long id){
        TagListening tagListening = tagListeningJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 수신 태그"));

        if(!accountContextService.isOwner(tagListening.getAccount()) && !accountContextService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        return TagListeningReadDto.Response.fromEntity(tagListening);
    }

    public Page<TagListeningReadDto.Response> readAllByPaging(Pageable pageable){
        Page<TagListening> all = tagListeningJpaRepository.findAll(pageable);
        List<TagListeningReadDto.Response> responseList = all.stream()
                .map(Response::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(responseList, all.getPageable(), all.getTotalElements());
    }

    public List<TagListeningReadDto.Response> readByAccountId(Long accountId) {
        List<TagListening> all = tagListeningJpaRepository.findAllByAccountId(accountId);
        return all.stream().map(Response::fromEntity).collect(Collectors.toList());
    }

    public List<TagListeningReadDto.Response> readMy() {
        Long accountId = accountContextService.getContextAccount().getAccountId();
        return readByAccountId(accountId);
    }
}
