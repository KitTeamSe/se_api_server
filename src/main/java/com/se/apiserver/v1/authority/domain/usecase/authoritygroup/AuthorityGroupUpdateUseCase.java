package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupUpdateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupUpdateUseCase {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public AuthorityGroupReadDto.Response update(AuthorityGroupUpdateDto.Request request) {
        validateDuplicateGroupName(request.getName());
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));
        updateDescriptionIfExist(authorityGroup, request.getDescription());
        updateNameIfExist(authorityGroup, request.getName());
        authorityGroupJpaRepository.save(authorityGroup);
        return AuthorityGroupReadDto.Response.fromEntity(authorityGroup);
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
