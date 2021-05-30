package com.se.apiserver.v1.division.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
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
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Division extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long divisionId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "open_subject_id", referencedColumnName = "openSubjectId")
  private OpenSubject openSubject;

  @Column(nullable = false)
  private Integer divisionNumber;

  @Column(nullable = false)
  private Integer deployedTeachingTime;

  @Column(nullable = false)
  private Boolean autoCreated;

  @Size(max = 255)
  private String note;

  public Division(OpenSubject openSubject, Integer divisionNumber, Integer deployedTeachingTime, Boolean autoCreated){
    this.openSubject = openSubject;
    this.divisionNumber = divisionNumber;
    validateDeployedTeachingTime(deployedTeachingTime);
    this.deployedTeachingTime = deployedTeachingTime;
    this.autoCreated = autoCreated;
  }

  public Division(OpenSubject openSubject, Integer divisionNumber, Integer deployedTeachingTime, Boolean autoCreated, @Size(max = 255) String note){
    this(openSubject, divisionNumber, deployedTeachingTime, autoCreated);
    this.note = note;
  }

  public void validateDeployedTeachingTime(Integer deployedTeachingTime){
    if(deployedTeachingTime == null || deployedTeachingTime < 0)
      throw new BusinessException(DivisionErrorCode.INVALID_DEPLOYED_TEACHING_TIME);
  }

  public void updateDeployedTeachingTime(Integer deployedTeachingTime){
    this.deployedTeachingTime = deployedTeachingTime;
  }

//  public void deleteFromOpenSubject(){
//    if(this.openSubject.getDivisions().contains(this))
//      this.openSubject.getDivisions().remove(this);
//  }
}
