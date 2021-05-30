package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupUpdateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupUpdateService {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public Long update(AuthorityGroupUpdateDto.Request request) {
        validateDuplicateGroupName(request.getName());
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));
        updateDescriptionIfExist(authorityGroup, request.getDescription());
        updateNameIfExist(authorityGroup, request.getName());

        return authorityGroupJpaRepository.save(authorityGroup).getAuthorityGroupId();
    }

    private void updateNameIfExist(AuthorityGroup authorityGroup, String name) {
        if(name != null)
            authorityGroup.updateName(name);
    }

    private void updateDescriptionIfExist(AuthorityGroup authorityGroup, String description) {
        if(description != null)
            authorityGroup.updateDescription(description);
    }

    private void validateDuplicateGroupName(String name) {
        if (authorityGroupJpaRepository.findByName(name).isPresent())
            throw new BusinessException(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME);
    }

}
