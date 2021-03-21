package com.se.apiserver.repository.account;

import com.se.apiserver.domain.entity.account.AccountVerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountVerifyTokenJpaRepository extends JpaRepository<AccountVerifyToken, Long> {
  AccountVerifyToken findFirstByToken(String token);

}
