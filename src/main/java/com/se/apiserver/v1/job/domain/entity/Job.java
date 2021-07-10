package com.se.apiserver.v1.job.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long employmentInfoId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
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

  @Builder
  public Job(Long employmentInfoId, Account account, @Size(min = 3, max = 30) String position,
      @Size(min = 10, max = 255) String blogUrl, @Size(min = 10, max = 255) String githubUrl, LocalDate dateGraduation,
      @Size(min = 3, max = 30) String currentCompany, @Size(min = 2, max = 10) String languageTestType,
      @Size(min = 2, max = 10) String languageScore, Double avrGrades) {
    this.employmentInfoId = employmentInfoId;
    this.account = account;
    this.position = position;
    this.blogUrl = blogUrl;
    this.githubUrl = githubUrl;
    this.dateGraduation = dateGraduation;
    this.currentCompany = currentCompany;
    this.languageTestType = languageTestType;
    this.languageScore = languageScore;
    this.avrGrades = avrGrades;
  }
}
