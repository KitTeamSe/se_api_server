package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
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
public class AuthorityGroupAuthorityMappingReadUseCase {
    private final AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;


    public AuthorityGroupAuthorityMappingReadDto.Response read(Long id){
        AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = authorityGroupAuthorityMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING));
        return AuthorityGroupAuthorityMappingReadDto.Response.fromEntity(authorityGroupAuthorityMapping);
    }

    public PageImpl readAll(Pageable pageable){
        Page<AuthorityGroupAuthorityMapping> all = authorityGroupAuthorityMappingJpaRepository.findAll(pageable);
        List<AuthorityGroupAuthorityMappingReadDto.Response> responseList = all.stream()
                .map(a -> AuthorityGroupAuthorityMappingReadDto.Response.fromEntity(a))
                .collect(Collectors.toList());
        return new PageImpl<>(responseList, all.getPageable(), all.getTotalElements());
    }
}
