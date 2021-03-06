package com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupAccountMappingReadService {
    private final AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

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

    public boolean isAccountMappedToGroup(Long accountId, AuthorityGroup authorityGroup){
        List<AuthorityGroupAccountMapping> maps = getMappedAuthorityGroupsByAccountId(accountId);
        for(AuthorityGroupAccountMapping agam : maps){
            if(agam.getAuthorityGroup() == authorityGroup)
                return true;
        }
        return false;
    }

    public List<AuthorityGroupAccountMapping> getMappedAuthorityGroupsByAccountId(Long accountId){
        accountJpaRepository.findById(accountId).orElseThrow(() -> new BusinessException(
            AccountErrorCode.NO_SUCH_ACCOUNT));
        return authorityGroupAccountMappingJpaRepository.findByAccountId(accountId);
    }
}
