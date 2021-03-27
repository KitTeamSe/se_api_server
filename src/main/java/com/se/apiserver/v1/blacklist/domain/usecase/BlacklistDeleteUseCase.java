package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.domain.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistDeleteUseCase {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public void delete(Long id) {
        Blacklist blacklist = blacklistJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BlacklistErrorCode.NO_SUCH_BLACKLIST));
        blacklistJpaRepository.delete(blacklist);
    }
}
