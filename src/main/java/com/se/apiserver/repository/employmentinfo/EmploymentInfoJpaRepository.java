package com.se.apiserver.repository.employmentinfo;

import com.se.apiserver.domain.entity.employmentinfo.EmploymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentInfoJpaRepository extends JpaRepository<EmploymentInfo, Long> {
}
