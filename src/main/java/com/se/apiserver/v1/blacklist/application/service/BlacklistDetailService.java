package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlacklistDetailService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public BlacklistDetailService(
        BlacklistJpaRepository blacklistJpaRepository) {
        this.blacklistJpaRepository = blacklistJpaRepository;
    }

    public boolean isBannedIp(String ip){
        return blacklistJpaRepository.findByIp(ip).isPresent();
    }

    public boolean isBannedUser(String idString) {
        return blacklistJpaRepository.findByIdString(idString).isPresent();
    }
}
