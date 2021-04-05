package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAuthorityMappingCreateUseCase {
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
