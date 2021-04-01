package com.se.apiserver.v1.timetable.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTable extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timeTableId;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer year;

  @Column(nullable = false)
  private Integer semester;

  // 생성자 계정 (MASTER)
  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
  private Account account;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TimeTableStatus status;

  @Builder
  public TimeTable(Long timeTableId,
      @Size(min = 2, max = 20) String name, Integer year, Integer semester,
      Account account, TimeTableStatus status) {
    this.timeTableId = timeTableId;
    this.name = name;
    this.year = year;
    this.semester = semester;
    this.account = account;
    this.status = status;
  }
}
