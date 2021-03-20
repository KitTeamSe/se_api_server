package com.se.apiserver.domain.entity.career;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.job.Job;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Career extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long careerId;

  @ManyToOne
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
