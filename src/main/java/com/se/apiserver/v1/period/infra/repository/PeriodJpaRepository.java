package com.se.apiserver.v1.period.infra.repository;

import com.se.apiserver.v1.period.domain.entity.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodJpaRepository extends JpaRepository<Period, Long> {
  @Override
  Optional<Period> findById(Long id);

  Optional<Period> findByPeriodOrder(Integer periodOrder);

  Optional<Period> findByName(String name);
}
