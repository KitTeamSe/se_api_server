package com.se.apiserver.domain.entity.post;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.board.Board;
import com.se.apiserver.domain.entity.common.Anonymous;
import com.se.apiserver.domain.entity.reply.Reply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "boardId", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String text;

    @Embedded
    private Anonymous anonymous;

    @Column(length = 20, nullable = false)
    private String ip;

    @Column(length = 10, nullable = false)
    private PostType isNotice;

    @Column(nullable = false)
    private Integer views;

    @Column(length = 10, nullable = false)
    private PostStatus status;

    @OneToMany(mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

}
