package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistDetailService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public boolean isBaned(String ip){
        return blacklistJpaRepository.findByIp(ip).isPresent();
    }
}
