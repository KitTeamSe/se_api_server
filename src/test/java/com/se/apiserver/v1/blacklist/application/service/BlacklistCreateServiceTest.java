package com.se.apiserver.v1.blacklist.application.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BlacklistCreateServiceTest {
    @Mock
    BlacklistJpaRepository blacklistJpaRepository;
    @InjectMocks
    BlacklistCreateService blacklistCreateService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 등록_성공() {
        //given
        //when
        BlacklistReadDto.Response response = blacklistCreateService.create(BlacklistCreateDto.Request.builder().ip("0.0.0.0").reason("광고성 댓글").build());

        //then
        Assertions.assertThat(response.getReason()).isEqualTo(response.getReason());
        Assertions.assertThat(response.getIp()).isEqualTo(response.getIp());
    }

    @Test
    void 등록_중복_실패() {
        //given
        //when
        Blacklist blacklist = mock(Blacklist.class);
        when(blacklistJpaRepository.findByIp("0.0.0.0")).thenReturn(Optional.of(blacklist));
        //then
        Assertions.assertThatThrownBy(() -> {
            blacklistCreateService.create(BlacklistCreateDto.Request.builder().ip("0.0.0.0").build());
        }).isInstanceOf(BusinessException.class).hasMessage(BlacklistErrorCode.DUPLICATED_BLACKLIST.getMessage());
    }
}