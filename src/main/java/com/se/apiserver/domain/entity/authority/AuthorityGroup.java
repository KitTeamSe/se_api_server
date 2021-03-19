package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityGroup extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityGroupId;

    @Column(length = 30, nullable = false, unique = true)
    @Size(min = 2, max = 30)
    private String name;

    @Column(length = 100, nullable = false)
    @Size(min = 2, max = 100)
    private String description;

}
