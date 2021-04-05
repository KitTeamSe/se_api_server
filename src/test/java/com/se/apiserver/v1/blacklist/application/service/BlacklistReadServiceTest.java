package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BlacklistReadServiceTest {
    @Autowired
    BlacklistJpaRepository blacklistJpaRepository;
    @Autowired
    BlacklistReadService blacklistReadService;

    @Test
    void 조회_성공() {
        //given
        Blacklist blacklist = blacklistJpaRepository.save( new Blacklist("128.0.0.1", "광고성댓글"));
        //when
        BlacklistReadDto.Response read = blacklistReadService.read(blacklist.getBlacklistId());
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
            blacklistReadService.read(2L);
        }).isInstanceOf(BusinessException.class).hasMessage(BlacklistErrorCode.NO_SUCH_BLACKLIST.getMessage());
    }

    @Test
    void 전체_조회_성공() {
        //given
        Blacklist blacklist = blacklistJpaRepository.save( new Blacklist("128.0.0.1", "광고성댓글"));
        Blacklist blacklist2 = blacklistJpaRepository.save( new Blacklist("128.0.0.2", "광고성댓글"));
        //when
        PageImpl responses = blacklistReadService.readAll(PageRequest.builder()
        .size(10)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());
        //then
        Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
    }
}