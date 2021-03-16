package com.se.apiserver.domain.entity.authorityGroup;

import com.se.apiserver.domain.entity.menu.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAuthorityGroupMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuAuthorityGroupMappingId;

    @ManyToOne
    @JoinColumn(name = "authority_group_id", referencedColumnName = "authorityGroupId", nullable = false)
    private AuthorityGroup authorityGroup;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menuId", nullable = false)
    private Menu menu;
}
