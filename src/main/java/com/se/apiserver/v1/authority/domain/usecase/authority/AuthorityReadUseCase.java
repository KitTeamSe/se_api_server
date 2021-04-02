package com.se.apiserver.v1.authority.domain.usecase.authority;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityUpdateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityReadUseCase {

    private final AuthorityJpaRepository authorityJpaRepository;

    public AuthorityReadDto.Response read(Long id){
        Authority authority = authorityJpaRepository.findById(id).orElseThrow(() -> new BusinessException(AuthorityErrorCode.NO_SUCH_AUTHORITY));
        return AuthorityReadDto.Response.fromEntity(authority);
    }

    public PageImpl<AuthorityReadDto.Response> readAll(Pageable pageable){
        Page<Authority> authorities = authorityJpaRepository.findAll(pageable);

        List<AuthorityReadDto.Response> responseList = authorities.stream()
                .map(a -> AuthorityReadDto.Response.fromEntity(a))
                .collect(Collectors.toList());
        return new PageImpl(responseList, authorities.getPageable(), authorities.getTotalElements());
    }

}
