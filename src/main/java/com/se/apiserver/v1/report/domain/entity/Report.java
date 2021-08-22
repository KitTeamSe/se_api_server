package com.se.apiserver.v1.report.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ReportType", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reportId;

  @Column(length = 255, nullable = false)
  @Size(min = 5, max = 255)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportStatus reportStatus;

  @Column
  @Enumerated(EnumType.STRING)
  private ReportResult reportResult;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "reporter_id", referencedColumnName = "accountId", nullable = false)
  private Account reporter;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "processor_id", referencedColumnName = "accountId")
  private Account processor;

  public abstract Long getTargetId();

  @Transient
  public ReportType getReportType(){
    return ReportType.valueOf(this.getClass().getAnnotation(DiscriminatorValue.class).value());
  }

  public Report(String description,
      ReportStatus reportStatus, ReportResult reportResult,
      Account reporter, Account processor) {
    this.description = description;
    this.reportStatus = reportStatus;
    this.reportResult = reportResult;
    this.reporter = reporter;
    this.processor = processor;
  }

  protected Report(@Size(min = 5, max = 255) String description, Account reporter) {
    this(description, ReportStatus.SUBMITTED, null, reporter, null);
  }

  public void updateDescription(String description){
    this.description = description;
  }

  public void updateReportStatus(ReportStatus reportStatus){
    this.reportStatus = reportStatus;
  }

  public void updateReportResult(ReportResult reportResult) {this.reportResult = reportResult;}

  public void updateProcessor(Account processor){
    this.processor = processor;
  }
}
