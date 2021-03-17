package com.se.apiserver.domain.entity.account;

import com.se.apiserver.domain.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AccountReceiveTagMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AccountReceiveTagMappingId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
    private Post tag;
}
