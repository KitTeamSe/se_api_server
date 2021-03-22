package com.se.apiserver.v1.account.infra.repository;

import com.se.apiserver.v1.account.domain.entity.Account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

  @Override
  Optional<Account> findById(Long aLong);

  Optional<Account> findByIdString(String idString);

  Optional<Account> findByEmail(String email);

  List<Account> findByNickname(String nickname);

  List<Account> findByStudentId(String studentId);
}
