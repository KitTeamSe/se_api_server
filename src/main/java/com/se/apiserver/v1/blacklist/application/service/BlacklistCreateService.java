package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistCreateService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    @Transactional
    public BlacklistReadDto.Response create(BlacklistCreateDto.Request request) {
        validateDuplicateIp(request.getIp());
        Blacklist blacklist = new Blacklist(request.getIp(), request.getReason());
        blacklistJpaRepository.save(blacklist);
        return BlacklistReadDto.Response.fromEntity(blacklist);
    }

    private void validateDuplicateIp(String ip) {
        if(blacklistJpaRepository.findByIp(ip).isPresent())
            throw new BusinessException(BlacklistErrorCode.DUPLICATED_BLACKLIST);
    }
}
