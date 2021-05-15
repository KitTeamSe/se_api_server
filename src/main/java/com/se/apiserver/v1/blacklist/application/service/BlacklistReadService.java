package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistReadService {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public BlacklistReadDto.Response read(Long id) {
        Blacklist blacklist = blacklistJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BlacklistErrorCode.NO_SUCH_BLACKLIST));
        return BlacklistReadDto.Response.fromEntity(blacklist);

    }

    public PageImpl readAll(Pageable pageable) {
        Page<Blacklist> all = blacklistJpaRepository.findAll(pageable);
        List<BlacklistReadDto.Response> responseList = all.stream()
                .map(b -> BlacklistReadDto.Response.fromEntity(b))
                .collect(Collectors.toList());

        return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
    }
}
