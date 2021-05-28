package com.se.apiserver.v1.notice.infra.repository;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<Notice, Long> {

}

