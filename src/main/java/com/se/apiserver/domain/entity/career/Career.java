package com.se.apiserver.domain.entity.career;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.job.Job;
import java.time.LocalDate;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
