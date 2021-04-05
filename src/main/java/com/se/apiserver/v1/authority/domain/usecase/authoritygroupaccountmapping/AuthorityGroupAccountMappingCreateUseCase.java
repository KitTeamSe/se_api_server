package com.se.apiserver.v1.authority.domain.usecase.authoritygroupaccountmapping;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAccountMappingCreateUseCase {
    private final AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    public Long create(AuthorityGroupAccountMappingCreateDto.Request request){
        validateAlreadyExistMapping(request.getAccountId(), request.getGroupId());

        Account account = accountJpaRepository.findById(request.getAccountId())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(request.getGroupId())
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));

        AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account, authorityGroup);
        authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);

        return authorityGroupAccountMapping.getAuthorityGroupAccountMappingId();
    }

    private void validateAlreadyExistMapping(Long accountId, Long groupId) {
        if(authorityGroupAccountMappingJpaRepository.findByAccountIdAndAuthorityGroupId(accountId, groupId).isPresent())
            throw new BusinessException(AuthorityGroupAccountMappingErrorCode.ALREADY_EXIST);
    }

}
