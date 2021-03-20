package com.se.apiserver.domain.entity.post;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.board.Board;
import com.se.apiserver.domain.entity.common.Anonymous;
import com.se.apiserver.domain.entity.reply.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    @Size(min = 3, max = 50)
    private String title;

    @Column(length = 2000, nullable = false)
    @Size(min = 5, max = 2000)
    private String text;

    @Embedded
    private Anonymous anonymous;

    @Column(length = 20, nullable = false)
    @Size(min = 4, max = 20)
    private String ip;

    @Column(length = 10, nullable = false)
    @Size(min = 2, max = 10)
    @Enumerated(EnumType.STRING)
    private PostType isNotice;

    @Column(nullable = false)
    private Integer views;

    @Column(length = 10, nullable = false)
    @Size(min = 2, max = 10)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @OneToMany(mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Post(Long postId, Board board, Account account, @Size(min = 3, max = 50) String title, @Size(min = 5, max = 2000) String text, Anonymous anonymous, @Size(min = 4, max = 20) String ip, @Size(min = 2, max = 10) PostType isNotice, Integer views, @Size(min = 2, max = 10) PostStatus status, List<Reply> replies) {
        this.postId = postId;
        this.board = board;
        this.account = account;
        this.title = title;
        this.text = text;
        this.anonymous = anonymous;
        this.ip = ip;
        this.isNotice = isNotice;
        this.views = views;
        this.status = status;
        this.replies = replies;
    }
}
