package com.se.apiserver.v1.account.infra.repository;

import com.se.apiserver.v1.account.domain.entity.AccountVerifyToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountVerifyTokenJpaRepository extends JpaRepository<AccountVerifyToken, Long> {
  Optional<AccountVerifyToken> findFirstByToken(String token);

}
