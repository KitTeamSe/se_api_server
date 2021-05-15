package com.se.apiserver.v1.account.infra.repository;

import com.se.apiserver.v1.account.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
}
