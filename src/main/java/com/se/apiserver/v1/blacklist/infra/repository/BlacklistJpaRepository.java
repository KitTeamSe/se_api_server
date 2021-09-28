package com.se.apiserver.v1.blacklist.infra.repository;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistJpaRepository extends JpaRepository<Blacklist, Long> {
    List<Blacklist> findByIpAndReleaseDateAfter(String ip, LocalDateTime today);
    List<Blacklist> findByAccountAndReleaseDateAfter(Account account, LocalDateTime today);
}
