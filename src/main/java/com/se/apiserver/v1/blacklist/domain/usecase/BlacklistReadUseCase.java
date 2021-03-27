package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.domain.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistReadUseCase {

    private final BlacklistJpaRepository blacklistJpaRepository;

    public BlacklistReadDto.Response read(Long id) {
        Blacklist blacklist = blacklistJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BlacklistErrorCode.NO_SUCH_BLACKLIST));
        return BlacklistReadDto.Response.fromEntity(blacklist);

    }

    public List<BlacklistReadDto.Response> readAll() {
        List<Blacklist> blacklists = blacklistJpaRepository.findAll();
        return blacklists.stream().map(b -> {return BlacklistReadDto.Response.fromEntity(b);}).collect(Collectors.toList());
    }
}
