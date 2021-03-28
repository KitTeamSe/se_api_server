package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
