package com.se.apiserver.v1.blacklist.domain.usecase;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.domain.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BlacklistReadUseCaseTest {
    @Autowired
    BlacklistJpaRepository blacklistJpaRepository;
    @Autowired
    BlacklistReadUseCase blacklistReadUseCase;

    @Test
    void 조회_성공() {
        //given
        Blacklist blacklist = blacklistJpaRepository.save(Blacklist.builder()
                .ip("127.0.0.1")
                .reason("비정상적접근")
                .build());
        //when
        BlacklistReadDto.Response read = blacklistReadUseCase.read(blacklist.getBlacklistId());
        //then
        Assertions.assertThat(blacklist.getBlacklistId()).isEqualTo(read.getId());
        Assertions.assertThat(blacklist.getIp()).isEqualTo(read.getIp());
        Assertions.assertThat(blacklist.getReason()).isEqualTo(read.getReason());
    }

    @Test
    void 조회_미존재_실패() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            blacklistReadUseCase.read(2L);
        }).isInstanceOf(BusinessException.class).hasMessage(BlacklistErrorCode.NO_SUCH_BLACKLIST.getMessage());
    }

    @Test
    void 전체_조회_성공() {
        //given
        Blacklist blacklist = blacklistJpaRepository.save(Blacklist.builder()
                .ip("127.0.0.1")
                .reason("비정상적접근")
                .build());
        Blacklist blacklist2 = blacklistJpaRepository.save(Blacklist.builder()
                .ip("127.0.0.2")
                .reason("비정상적접근")
                .build());
        //when
        List<BlacklistReadDto.Response> responses = blacklistReadUseCase.readAll();
        //then
        Assertions.assertThat(responses.size()).isEqualTo(2);
    }
}