package com.se.apiserver.domain.entity.employmentinfo;

import com.se.apiserver.domain.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmploymentInfo extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employmentInfoId;
}
