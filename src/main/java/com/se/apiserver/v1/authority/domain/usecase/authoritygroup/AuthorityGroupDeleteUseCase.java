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
public class AuthorityGroupDeleteUseCase {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public boolean delete(Long id) {
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));

        if(authorityGroup.getType() == AuthorityGroupType.ANONYMOUS)
            throw new BusinessException(AuthorityGroupErrorCode.CAN_NOT_DELETE_ANONYMOUS_GROUP);

        if(authorityGroup.getType() == AuthorityGroupType.DEFAULT)
            throw new BusinessException(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP);

        authorityGroupJpaRepository.delete(authorityGroup);
        return true;
    }

}
