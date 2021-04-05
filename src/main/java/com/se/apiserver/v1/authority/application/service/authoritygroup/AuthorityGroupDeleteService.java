package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityGroupDeleteService {

    private final AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Transactional
    public boolean delete(Long id) {
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP));
        authorityGroup.remove();
        authorityGroupJpaRepository.delete(authorityGroup);
        return true;
    }

}
