package com.se.apiserver.v1.common.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Component
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AccountGenerateEntity extends BaseEntity{

  @CreatedBy
  private Long createdAccountBy;

  @LastModifiedBy
  private Long lastModifiedAccountBy;
}
