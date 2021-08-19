package com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAccountMappingDeleteService {
    private final AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;

    @Transactional
    public void delete(Long id){
        AuthorityGroupAccountMapping authorityGroupAccountMapping = authorityGroupAccountMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING));
        authorityGroupAccountMappingJpaRepository.delete(authorityGroupAccountMapping);
    }
}
