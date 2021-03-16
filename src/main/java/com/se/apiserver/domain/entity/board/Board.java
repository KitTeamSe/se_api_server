package com.se.apiserver.domain.entity.board;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 10)
    @Size(min = 2, max = 10)
    private String name;

    @Column(length = 10)
    @Size(min = 2, max = 10)
    private BoardStatus status;

    private Integer menuOrder;

    @ManyToOne
    @JoinColumn(name = "registrant_id", referencedColumnName = "account_id")
    private Account registrantAccount;

    @ManyToOne
    @JoinColumn(name = "last_modified_account_id", referencedColumnName = "account_id")
    private Account lastModifiedAccount;
}
