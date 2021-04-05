package com.se.apiserver.v1.tag.domain.entity;


import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagListening extends AccountGenerateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagListeningId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
    private Tag tag;

    public TagListening(Account account, Tag tag) {
        this.account = account;
        this.tag = tag;
    }
}
