package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
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
public class AuthorityGroupCreateUseCase {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public AuthorityGroupReadDto.Response create(AuthorityGroupCreateDto.Request request) {
        if (authorityGroupJpaRepository.findByName(request.getName()).isPresent())
            throw new BusinessException(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME);

        AuthorityGroup authorityGroup = AuthorityGroup.builder()
                .description(request.getDescription())
                .name(request.getName())
                .type(AuthorityGroupType.NORMAL)
                .build();
        authorityGroupJpaRepository.save(authorityGroup);

        return AuthorityGroupReadDto.Response.fromEntity(authorityGroup);
    }

}
