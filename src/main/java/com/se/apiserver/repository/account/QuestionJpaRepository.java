package com.se.apiserver.repository.account;

import com.se.apiserver.domain.entity.account.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
}
