package com.se.apiserver.domain.entity.report;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ReportAccountMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportAccountMappingId;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "reportId", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account account;
}
