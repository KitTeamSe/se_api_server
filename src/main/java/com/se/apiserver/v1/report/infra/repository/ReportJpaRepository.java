package com.se.apiserver.v1.report.infra.repository;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.report.domain.entity.Report;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
  @Override
  Optional<Report> findById(Long reportId);
  Page<Report> findAllByReporter(Pageable pageable, Account account);
}
