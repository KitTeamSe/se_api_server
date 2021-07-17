package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BlacklistDeleteServiceTest {
    @Autowired
    BlacklistDeleteService blacklistDeleteService;
    @Autowired
    BlacklistJpaRepository blacklistJpaRepository;

    @Test
    void 삭제_성공() {
        //given
        Blacklist blacklist = blacklistJpaRepository.save( new Blacklist("128.0.0.1", "광고성댓글"));
        //when
        Long id = blacklist.getBlacklistId();
        blacklistDeleteService.delete(id);
        //then
        Assertions.assertThat(blacklistJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }
    @Test
    void 삭제_미존재_실패() {
        //given
        //when
        Long id = 10L;
        //then
        Assertions.assertThatThrownBy(() -> {
            blacklistDeleteService.delete(id);
        }).isInstanceOf(BusinessException.class).hasMessage(BlacklistErrorCode.NO_SUCH_BLACKLIST.getMessage());
    }

}