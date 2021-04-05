package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAuthorityMappingDeleteService {
    private final AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;

    public boolean delete(Long id){
        AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = authorityGroupAuthorityMappingJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING));

        authorityGroupAuthorityMappingJpaRepository.delete(authorityGroupAuthorityMapping);
        return true;
    }
}
