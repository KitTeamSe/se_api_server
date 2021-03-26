package com.se.apiserver.v1.subject.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class OpenSubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long openSubjectId;

  @Column(nullable = false)
  private Long timeTableId;

  @Column(nullable = false)
  private Long subjectId;

  @Column(nullable = false)
  private Integer numberOfDivision;

  @Column(nullable = false)
  private Integer teachingTimePerWeek;
}
