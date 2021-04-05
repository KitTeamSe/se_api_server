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

    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public Authority create(AuthorityCreateDto.Request request){
        validateDuplicateNameEng(request.getNameEng());
        validateDuplicateNameKor(request.getNameKor());
        Authority authority = new Authority(request.getNameEng(), request.getNameKor());
        authorityJpaRepository.save(authority);
        return authority;
    }

    private void validateDuplicateNameEng(String nameEng) {
        if(authorityJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_ENG);
    }

    private void validateDuplicateNameKor(String nameKor) {
        if(authorityJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(AuthorityErrorCode.DUPLICATED_NAME_KOR);
    }
}
