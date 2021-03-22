package com.se.apiserver.v1.account.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long questionId;

  @Column(length = 100)
  @Size(min = 2, max = 100)
  private String text;

  @Builder
  public Question(Long questionId, @Size(min = 2, max = 100) String text) {
    this.questionId = questionId;
    this.text = text;
  }
}
