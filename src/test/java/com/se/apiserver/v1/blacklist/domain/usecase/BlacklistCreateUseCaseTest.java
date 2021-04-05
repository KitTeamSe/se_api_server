package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.domain.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BlacklistCreateUseCaseTest {
    @Autowired
    BlacklistCreateUseCase blacklistCreateUseCase;
    @Autowired
    BlacklistJpaRepository blacklistJpaRepository;

    @Test
    void 등록_성공() {
        //given
        //when
        BlacklistReadDto.Response response = blacklistCreateUseCase.create(BlacklistCreateDto.Request.builder().ip("127.0.0.1").reason("광고성댓글").build());
        Blacklist blacklist = blacklistJpaRepository.findById(response.getId()).get();
        //then
        Assertions.assertThat(blacklist.getBlacklistId()).isEqualTo(response.getId());
        Assertions.assertThat(blacklist.getIp()).isEqualTo(response.getIp());
    }

    @Test
    void 등록_중복_실패() {
        //given
        //when
        blacklistJpaRepository.save( new Blacklist("128.0.0.1", "광고성댓글"));
        //then
        Assertions.assertThatThrownBy(() -> {
            BlacklistReadDto.Response response = blacklistCreateUseCase.create(BlacklistCreateDto.Request.builder().ip("128.0.0.1").build());
        }).isInstanceOf(BusinessException.class).hasMessage(BlacklistErrorCode.DUPLICATED_BLACKLIST.getMessage());
    }
}