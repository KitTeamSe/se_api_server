package com.se.apiserver.v1.authority.application.service.authority;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.application.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.application.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityReadService {

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
