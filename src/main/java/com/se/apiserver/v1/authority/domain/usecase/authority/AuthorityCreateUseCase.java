package com.se.apiserver.v1.authority.domain.usecase.authority;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityCreateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityCreateUseCase {

    @Value("${spring.authority.menu.eng.prefix}")
    private String nameEngPrefix;

    @Value("${spring.authority.menu.eng.postfix}")
    private String nameEngPostfix;

    @Value("${spring.authority.menu.kor.postfix}")
    private String nameKorPostfix;

    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public Authority create(AuthorityCreateDto.Request request){
        if(authorityJpaRepository.findByNameEng(buildAuthorityNameEng(request.getNameEng())).isPresent())
            throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_ENG);

        if(authorityJpaRepository.findByNameKor(buildAuthorityNameKor(request.getNameKor())).isPresent())
            throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_KOR);


        Authority authority = Authority.builder()
                .nameEng(buildAuthorityNameEng(request.getNameEng()))
                .nameKor(buildAuthorityNameKor(request.getNameKor()))
                .build();

        if(request.getMenu() != null)
            authority.updateMenu(request.getMenu());

        authorityJpaRepository.save(authority);
        return authority;
    }

    private String buildAuthorityNameEng(String menuName) {
        return nameEngPrefix + "_" + menuName + "_" + nameEngPostfix;
    }
    private String buildAuthorityNameKor(String menuName) {
        return menuName + " " + nameKorPostfix;
    }
}
