package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistDetailService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public boolean isBaned(String ip){
        Set<String> blacklist = new HashSet<>(blacklistJpaRepository.findAll()
        .stream().map(b -> b.getIp()).collect(Collectors.toList()));
        return blacklist.contains(ip);
    }

}
