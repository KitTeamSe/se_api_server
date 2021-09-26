package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlacklistCreateService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public BlacklistCreateService(
        BlacklistJpaRepository blacklistJpaRepository) {
        this.blacklistJpaRepository = blacklistJpaRepository;
    }

    @Transactional
    public Long create(BlacklistCreateDto.Request request) {
        validateDuplicateIp(request.getIp());
        validateDuplicateUser(request.getIdString());

        Blacklist blacklist = new Blacklist(
            request.getIp(),
            request.getIdString(),
            request.getReason(),
            request.getReleaseDate()
        );

        blacklistJpaRepository.save(blacklist);
        return blacklist.getBlacklistId();
    }

    private void validateDuplicateIp(String ip) {
        if(ip != null && blacklistJpaRepository.findByIp(ip).isPresent())
            throw new BusinessException(BlacklistErrorCode.DUPLICATED_BLACKLIST);
    }

    private void validateDuplicateUser(String idString) {
        if(idString != null && blacklistJpaRepository.findByIdString(idString).isPresent())
            throw new BusinessException(BlacklistErrorCode.DUPLICATED_BLACKLIST);
    }
}
