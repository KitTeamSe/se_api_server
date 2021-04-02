package com.se.apiserver.v1.common.domain.entity;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

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
