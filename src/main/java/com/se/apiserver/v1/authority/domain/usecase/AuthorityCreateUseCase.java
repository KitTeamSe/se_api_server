package com.se.apiserver.v1.authority.domain.usecase;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityCreateUseCase {

    @Value("${spring.authority.menu.prefix}")
    private String namePrefix;

    @Value("${spring.authority.menu.postfix}")
    private String namePostfix;

    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public String createMenuAuthority(Menu menu){
        Authority authority = Authority.builder()
                .nameEng(buildAuthorityName(menu.getNameEng()))
                .nameKor(menu.getNameKor())
                .build();

        authority.updateMenu(menu);

        authorityJpaRepository.save(authority);
        return authority.getAuthority();
    }

    private String buildAuthorityName(String menuName) {
        return namePrefix + "_" + menuName + "_" + namePostfix;
    }
}
