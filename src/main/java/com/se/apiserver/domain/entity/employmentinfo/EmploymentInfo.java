package com.se.apiserver.domain.entity.employmentinfo;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmploymentInfo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long employmentInfoId;

  @OneToOne(fetch = FetchType.LAZY)
  private Account account;

  @Column(length = 30)
  @Size(min = 3, max = 30)
  private String position;

  @Column(length = 255)
  @Size(min = 10, max = 255)
  private String blogUrl;

  @Column(length = 255)
  @Size(min = 10, max = 255)
  private String githubUrl;

  private LocalDate dateGraduation;

  @Column(length = 30)
  @Size(min = 3, max = 30)
  private String currentCompany;

  @Column(length = 10)
  @Size(min = 2, max = 10)
  private String languageTestType;

  @Column(length = 10)
  @Size(min = 2, max = 10)
  private String languageScore;

  private Double avrGrades;


}
