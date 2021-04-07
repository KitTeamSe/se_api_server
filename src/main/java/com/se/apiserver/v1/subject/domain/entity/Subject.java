package com.se.apiserver.v1.subject.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
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
  @Column(nullable = false, unique = true)
  private String code;

  @Size(max = 50)
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
      @Size(min = 1, max = 50) String name, Integer grade, Integer semester, Integer credit) {

    validateGrade(grade);
    validateSemester(semester);
    validateCredit(credit);

    this.subjectId = subjectId;
    this.curriculum = curriculum;
    this.type = type;
    this.code = code;
    this.name = name;
    this.grade = grade;
    this.semester = semester;
    this.credit = credit;
  }

  public void validateGrade(Integer grade){
    if(grade < 0)
      throw new BusinessException(SubjectErrorCode.INVALID_GRADE);
  }

  public void validateSemester(Integer semester){
    if(semester < 0)
      throw new BusinessException(SubjectErrorCode.INVALID_SEMESTER);
  }

  public void validateCredit(Integer credit){
    if(credit < 0)
      throw new BusinessException(SubjectErrorCode.INVALID_CREDIT);
  }

  public void updateCurriculum(String curriculum){
    this.curriculum = curriculum;
  }

  public void updateType(SubjectType subjectType){
    this.type = subjectType;
  }

  public void updateCode(String code){
    this.code = code;
  }

  public void updateName(String name){
    this.name = name;
  }

  public void updateGrade(Integer grade){
    validateGrade(grade);
    this.grade = grade;
  }

  public void updateSemester(Integer semester){
    validateSemester(semester);
    this.semester = semester;
  }

  public void updateCredit(Integer credit){
    validateCredit(credit);
    this.credit = credit;
  }
}
