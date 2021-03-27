package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.domain.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistCreateUseCase {

    private final BlacklistJpaRepository blacklistJpaRepository;

    @Transactional
    public BlacklistReadDto.Response create(BlacklistCreateDto.Request request) {
        if(blacklistJpaRepository.findByIp(request.getIp()).isPresent())
            throw new BusinessException(BlacklistErrorCode.DUPLICATED_BLACKLIST);
        Blacklist blacklist = Blacklist.builder()
                .ip(request.getIp())
                .reason(request.getReason())
                .build();

        blacklistJpaRepository.save(blacklist);
        return BlacklistReadDto.Response.fromEntity(blacklist);
    }
}
