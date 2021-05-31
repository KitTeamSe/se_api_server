package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupCreateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupCreateService {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public Long create(AuthorityGroupCreateDto.Request request) {
        validateDuplicateGroupName(request.getName());
        AuthorityGroup authorityGroup = new AuthorityGroup(request.getName(), request.getDescription(), AuthorityGroupType.NORMAL);

        return authorityGroupJpaRepository.save(authorityGroup).getAuthorityGroupId();
    }

    private void validateDuplicateGroupName(String name) {
        if (authorityGroupJpaRepository.findByName(name).isPresent())
            throw new BusinessException(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME);
    }

}
