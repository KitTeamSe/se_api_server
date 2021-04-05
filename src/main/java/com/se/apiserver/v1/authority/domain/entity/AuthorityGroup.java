package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthorityGroup extends AccountGenerateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityGroupId;

    @Column(length = 30, nullable = false, unique = true)
    @Size(min = 2, max = 30)
    private String name;

    @Column(length = 100, nullable = false)
    @Size(min = 2, max = 100)
    private String description;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityGroupType type;

    public AuthorityGroup(@Size(min = 2, max = 30) String name, @Size(min = 2, max = 100) String description, AuthorityGroupType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void remove() {
        if(type == AuthorityGroupType.ANONYMOUS)
            throw new BusinessException(AuthorityGroupErrorCode.CAN_NOT_DELETE_ANONYMOUS_GROUP);

        if(type == AuthorityGroupType.DEFAULT)
            throw new BusinessException(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP);
    }
}
