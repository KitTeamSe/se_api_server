package com.se.apiserver.repository.account;

import com.se.apiserver.domain.entity.account.Account;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

  @Override
  Optional<Account> findById(Long aLong);

  Optional<Account> findByIdString(String idString);

  Optional<Account> findByEmail(String email);
}
