package com.se.apiserver.v1.subject.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Subject extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long subjectId;

  @Size(min = 2, max = 30)
  @Column(nullable = false)
  private String curriculum;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SubjectType type;

  @Size(min = 2, max = 30)
  @Column(nullable = false)
  private String code;

  @Size(min = 2, max = 30)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer grade;

  @Column(nullable = false)
  private Integer semester;

  @Column(nullable = false)
  private Integer credit;

  @Builder
  public Subject(Long subjectId,
      @Size(min = 2, max = 30) String curriculum,
      SubjectType type, @Size(min = 2, max = 30) String code,
      @Size(min = 2, max = 30) String name, Integer grade, Integer semester, Integer credit) {
    this.subjectId = subjectId;
    this.curriculum = curriculum;
    this.type = type;
    this.code = code;
    this.name = name;
    this.grade = grade;
    this.semester = semester;
    this.credit = credit;
  }
}