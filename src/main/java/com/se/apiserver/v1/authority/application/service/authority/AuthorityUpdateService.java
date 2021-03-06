package com.se.apiserver.v1.authority.application.service.authority;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.application.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.application.dto.authority.AuthorityUpdateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityUpdateService {

    @Value("${spring.authority.menu.eng.prefix}")
    private String nameEngPrefix;

    @Value("${spring.authority.menu.eng.postfix}")
    private String nameEngPostfix;

    @Value("${spring.authority.menu.kor.postfix}")
    private String nameKorPostfix;

    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public boolean update(AuthorityUpdateDto.Request request){
        Authority authority = authorityJpaRepository.findById(request.getId()).orElseThrow(() -> new BusinessException(AuthorityErrorCode.NO_SUCH_AUTHORITY));
        if(request.getNameEng() != null){
            String beNameEng = buildAuthorityNameEng(request.getNameEng());
            if(authorityJpaRepository.findByNameEng(beNameEng).isPresent())
                throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_ENG);
            authority.updateNameEng(beNameEng);
        }

        if(request.getNameKor() != null){
            String beNameKor = buildAuthorityNameKor(request.getNameKor());
            if(authorityJpaRepository.findByNameKor(beNameKor).isPresent())
                throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_KOR);
            authority.updateNameKor(beNameKor);
        }

        authorityJpaRepository.save(authority);
        return true;
    }

    private String buildAuthorityNameEng(String menuName) {
        return nameEngPrefix + "_" + menuName + "_" + nameEngPostfix;
    }
    private String buildAuthorityNameKor(String menuName) {
        return menuName + " " + nameKorPostfix;
    }
}
