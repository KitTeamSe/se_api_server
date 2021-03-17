package com.se.apiserver.domain.entity.noticerecord;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.account.AccountReceiveTagMapping;
import com.se.apiserver.domain.entity.fcm.Fcm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class NoticeRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteceRecordId;

    @ManyToOne
    @JoinColumn(name = "fcm_id", referencedColumnName = "fcmId", nullable = false)
    private Fcm fcm;

    @ManyToOne
    @JoinColumn(name = "Account_receive_tag_mapping_id", referencedColumnName = "AccountReceiveTagMappingId", nullable = false)
    private AccountReceiveTagMapping AccountReceiveTagMapping;


}
