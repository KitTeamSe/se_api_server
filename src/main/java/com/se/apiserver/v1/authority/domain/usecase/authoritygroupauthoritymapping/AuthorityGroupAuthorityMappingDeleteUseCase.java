package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAuthorityMappingDeleteUseCase {
    private final AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;

    public boolean delete(Long id){
        AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = authorityGroupAuthorityMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING));
        authorityGroupAuthorityMappingJpaRepository.delete(authorityGroupAuthorityMapping);
        return true;
    }
}
