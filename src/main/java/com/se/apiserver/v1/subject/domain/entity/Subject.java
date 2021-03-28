package com.se.apiserver.v1.subject.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
public class Subject {

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
}
