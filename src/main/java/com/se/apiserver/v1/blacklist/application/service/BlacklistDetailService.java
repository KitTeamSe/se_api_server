package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
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
        return blacklistJpaRepository.findByIpAndReleaseDateAfter(ip, now()).size() > 0 ? true : false ;
    }

    public boolean isBannedAccount(Account account) {
        return blacklistJpaRepository.findByAccountAndReleaseDateAfter(account, now()).size() > 0 ? true : false ;
    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }
}
