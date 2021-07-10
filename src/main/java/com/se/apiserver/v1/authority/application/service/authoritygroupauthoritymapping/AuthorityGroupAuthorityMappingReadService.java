package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.ArrayList;
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
public class AuthorityGroupAuthorityMappingReadService {
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

    public List<Authority> readAllAuthorityByAuthorityGroup(AuthorityGroup authorityGroup){
        List<Authority> result = new ArrayList<>();
        List<AuthorityGroupAuthorityMapping> all = authorityGroupAuthorityMappingJpaRepository.findAllByAuthorityGroup(authorityGroup);
        for(AuthorityGroupAuthorityMapping agam : all){
            result.add(agam.getAuthority());
        }
        return result;
    }
}
