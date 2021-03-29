package com.se.apiserver.v1.authority.domain.usecase.authoritygroupaccountmapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAccountMappingDeleteUseCase {
    private final AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;

    public boolean delete(Long id){
        AuthorityGroupAccountMapping authorityGroupAccountMapping = authorityGroupAccountMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING));
        authorityGroupAccountMappingJpaRepository.delete(authorityGroupAccountMapping);
        return true;
    }
}
