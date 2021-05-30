package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
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
public class AuthorityGroupReadService {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    public AuthorityGroupReadDto.Response read(Long id){
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(id).
                orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));
        return AuthorityGroupReadDto.Response.fromEntity(authorityGroup);
    }

    public PageImpl readAll(Pageable pageable){
        Page<AuthorityGroup> authorityGroups = authorityGroupJpaRepository.findAll(pageable);
        List<AuthorityGroupReadDto.Response> responseList = authorityGroups.stream()
                .map(a -> AuthorityGroupReadDto.Response.fromEntity(a))
                .collect(Collectors.toList());
        return new PageImpl(responseList, authorityGroups.getPageable(), authorityGroups.getTotalElements());
    }

    public List<AuthorityGroup> getAuthorityGroupsByType(AuthorityGroupType authorityGroupType){
        List<AuthorityGroup> list = authorityGroupJpaRepository.findByType(authorityGroupType);
        if(list == null || list.isEmpty())
            throw new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP);
        return list;
    }

    public AuthorityGroup getDefaultAuthorityGroup(){
        return getAuthorityGroupsByType(AuthorityGroupType.DEFAULT).get(0);
    }

}
