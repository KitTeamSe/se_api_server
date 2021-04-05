package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.application.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAuthorityMappingCreateService {
    private final AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    public Long create(AuthorityGroupAuthorityMappingCreateDto.Request request){
        validateAlreadyExistMapping(request.getAuthorityId(), request.getGroupId());
        Authority authority = authorityJpaRepository.findById(request.getAuthorityId())
                .orElseThrow(() -> new BusinessException(AuthorityErrorCode.NO_SUCH_AUTHORITY));
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(request.getGroupId())
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));

        AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = new AuthorityGroupAuthorityMapping(authority, authorityGroup);
        authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping);

        return authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId();
    }

    private void validateAlreadyExistMapping(Long authorityId, Long groupId) {
        if(authorityGroupAuthorityMappingJpaRepository.findByAuthorityAndAuthorityGroupId(authorityId, groupId).isPresent())
            throw new BusinessException(AuthorityGroupAuthorityMappingErrorCode.ALREADY_EXIST);
    }

}
