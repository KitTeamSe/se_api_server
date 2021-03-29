package com.se.apiserver.v1.authority.domain.usecase.authoritygroupaccountmapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
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
public class AuthorityGroupAccountMappingReadUseCase {
    private final AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;

    public AuthorityGroupAccountMappingReadDto.Response read(Long id){
        AuthorityGroupAccountMapping authorityGroupAccountMapping = authorityGroupAccountMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING));
        return AuthorityGroupAccountMappingReadDto.Response.fromEntity(authorityGroupAccountMapping);
    }

    public PageImpl readAll(Pageable pageable){
        Page<AuthorityGroupAccountMapping> all = authorityGroupAccountMappingJpaRepository.findAll(pageable);
        List<AuthorityGroupAccountMappingReadDto.Response> responseList = all.stream()
                .map(a -> AuthorityGroupAccountMappingReadDto.Response.fromEntity(a))
                .collect(Collectors.toList());
        return new PageImpl<>(responseList, all.getPageable(), all.getTotalElements());
    }
}
