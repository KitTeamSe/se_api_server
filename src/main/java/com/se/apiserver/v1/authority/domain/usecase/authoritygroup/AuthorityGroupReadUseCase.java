package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
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
public class AuthorityGroupReadUseCase {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    public AuthorityGroupReadDto.Response read(Long id){
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(id).
                orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));
        return AuthorityGroupReadDto.Response.fromEntity(authorityGroup);
    }

    public PageImpl readAll(Pageable pageable){
        Page<AuthorityGroup> authorityGroups = authorityGroupJpaRepository.findAll(pageable);
        List<AuthorityGroupReadDto.Response> responseList = authorityGroups.stream()
                .map(a -> AuthorityGroupReadDto.Response.fromEntity(a))
                .collect(Collectors.toList());
        return new PageImpl(responseList, authorityGroups.getPageable(), authorityGroups.getTotalElements());
    }

}
