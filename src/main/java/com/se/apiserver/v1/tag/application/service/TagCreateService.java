package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.PermissionDeniedException;
import com.se.apiserver.v1.common.domain.exception.UniqueValueAlreadyExistsException;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagCreateService {

    private final TagJpaRepository tagJpaRepository;
    private final AccountContextService accountContextService;

    public TagCreateService(
        TagJpaRepository tagJpaRepository,
        AccountContextService accountContextService) {
        this.tagJpaRepository = tagJpaRepository;
        this.accountContextService = accountContextService;
    }

    @Transactional
    public Long create(TagCreateDto.Request request) {
        Set<String> authorities = accountContextService.getContextAuthorities();
        Tag tag = new Tag(request.getText());

        if (!tag.hasManageAuthority(authorities)) {
            throw new PermissionDeniedException("태그 생성 권한이 없습니다");
        }

        if(tagJpaRepository.findByText(tag.getText()).isPresent())
            throw new UniqueValueAlreadyExistsException("중복된 태그명입니다");

        tagJpaRepository.save(tag);
        return tag.getTagId();
    }
}
