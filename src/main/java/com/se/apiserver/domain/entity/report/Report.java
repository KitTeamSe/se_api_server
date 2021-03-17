package com.se.apiserver.domain.entity.report;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.post.Post;
import com.se.apiserver.domain.entity.reply.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 255, nullable = false)
    @Size(min = 2, max = 255)
    private String text;

    @Column(length = 20, nullable = false)
    @Size(min = 2, max = 20)
    private ReportStatus status;

    @Column(length = 20, nullable = false)
    @Size(min = 2, max = 20)
    private ProcessType processType;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account processor;

    @OneToMany(mappedBy = "report")
    private List<ReportAccountMapping> repoerters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account reported;

}
