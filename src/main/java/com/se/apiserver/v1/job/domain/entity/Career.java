package com.se.apiserver.v1.job.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import java.time.LocalDate;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Career extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long careerId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "emplyment_info_id", nullable = false)
  private Job job;

  @Column(length = 30, nullable = false)
  private String company;

  @Column(length = 30, nullable = false)
  private String duty;

  @Column(nullable = false)
  private LocalDate dateStart;

  @Column
  private LocalDate dateEnd;

  @Builder
  public Career(Long careerId, Job job, String company, String duty, LocalDate dateStart, LocalDate dateEnd) {
    this.careerId = careerId;
    this.job = job;
    this.company = company;
    this.duty = duty;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }
}
