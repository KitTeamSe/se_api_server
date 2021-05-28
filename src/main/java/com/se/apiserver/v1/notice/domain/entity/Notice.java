package com.se.apiserver.v1.notice.domain.entity;


import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(length = 50, nullable = false)
    @Size(min = 3, max = 50)
    private String title;

    @Column(length = 255, nullable = false)
    @Size(min = 2, max = 255)
    private String message;

    @Column(length = 255, nullable = false)
    @Size(min = 2, max = 255)
    private String url;

    @Builder
    public Notice(@Size(min = 3, max = 50) String title, @Size(min = 2, max = 255) String message, @Size(min = 2, max = 255) String url) {
        this.title = title;
        this.message = message;
        this.url = url;
    }
}
