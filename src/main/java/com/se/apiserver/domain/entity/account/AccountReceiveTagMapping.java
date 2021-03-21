package com.se.apiserver.domain.entity.account;

import com.se.apiserver.domain.entity.tag.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountReceiveTagMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountReceiveTagMappingId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
  private Tag tag;
}
