package com.se.apiserver.domain.entity.career;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.employmentinfo.EmploymentInfo;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
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
  @JoinColumn(name = "emplyment_info_id")
  private EmploymentInfo employmentInfo;

  @Column(length = 30)
  private String company;

  @Column(length = 30)
  private String duty;

  @Column
  private LocalDate dateStart;

  @Column
  private LocalDate dateEnd;
}
