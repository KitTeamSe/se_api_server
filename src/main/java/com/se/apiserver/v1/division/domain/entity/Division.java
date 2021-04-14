package com.se.apiserver.v1.division.domain.entity;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Division {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long divisionId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "open_subject_id", referencedColumnName = "openSubjectId", nullable = false)
  private OpenSubject openSubject;

  @Column(nullable = false)
  private Integer deployedTeachingTime;

  @Builder
  public Division(Long divisionId, OpenSubject openSubject, Integer deployedTeachingTime){

    validateDeployedTeachingTime(deployedTeachingTime);

    this.divisionId = divisionId;
    this.openSubject = openSubject;
    this.deployedTeachingTime = deployedTeachingTime;
  }

  public void validateDeployedTeachingTime(Integer deployedTeachingTime){
    if(deployedTeachingTime < 0)
      throw new BusinessException(DivisionErrorCode.INVALID_DEPLOYED_TEACHING_TIME);
  }

  public void updateDeployedTeachingTime(Integer deployedTeachingTime){
    this.deployedTeachingTime = deployedTeachingTime;
  }

}
