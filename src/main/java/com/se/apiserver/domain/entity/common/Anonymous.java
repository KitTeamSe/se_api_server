package com.se.apiserver.domain.entity.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
public class Anonymous {
    @Column(length = 20)
    private String anonymousId;

    @Column(length = 10)
    private String anonymousNickname;

    @Column(length = 20)
    private String anonymousPassword;

    @Column(length = 30)
    @Email
    private String anonymousEmail;
}
