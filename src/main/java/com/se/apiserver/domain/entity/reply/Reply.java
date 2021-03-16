package com.se.apiserver.domain.entity.reply;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.common.Anonymous;
import com.se.apiserver.domain.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 500, nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    @Embedded
    private Anonymous anonymous;

    @Column(length = 20, nullable = false)
    private String ip;

    @Column(length = 10, nullable = false)
    private ReplyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Reply> child;

    @Column(nullable = false)
    private Integer depth;

}
