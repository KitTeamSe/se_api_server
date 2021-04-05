package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.internal.Collections;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@UseCase
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
