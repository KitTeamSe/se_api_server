package com.se.apiserver.repository.account;

import com.se.apiserver.domain.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
}
