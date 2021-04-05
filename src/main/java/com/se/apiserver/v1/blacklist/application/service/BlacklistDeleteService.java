package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistDeleteService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public void delete(Long id) {
        Blacklist blacklist = blacklistJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BlacklistErrorCode.NO_SUCH_BLACKLIST));
        blacklistJpaRepository.delete(blacklist);
    }
}
