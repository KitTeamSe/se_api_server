package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupUpdateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityGroupUpdateUseCaseTest {
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityGroupUpdateUseCase authorityGroupUpdateUseCase;

    AuthorityGroup authorityGroup1;

    private void initData() {
        authorityGroup1 = new AuthorityGroup("권한1", "권한설명1", AuthorityGroupType.NORMAL);
        authorityGroupJpaRepository.save(authorityGroup1);
    }
    @Test
    void 수정_성공() {
        //given
        initData();
        //when
        authorityGroupUpdateUseCase.update(AuthorityGroupUpdateDto.Request.builder()
        .id(authorityGroup1.getAuthorityGroupId())
        .description("권한설명2")
        .name("권한2")
        .build());
        //then
        Assertions.assertThat(authorityGroup1.getName()).isEqualTo("권한2");
        Assertions.assertThat(authorityGroup1.getDescription()).isEqualTo("권한설명2");
    }

    @Test
    void 수정_설명_비업데이트_성공() {
        //given
        initData();
        //when
        authorityGroupUpdateUseCase.update(AuthorityGroupUpdateDto.Request.builder()
                .id(authorityGroup1.getAuthorityGroupId())
                .name("권한2")
                .build());
        //then
        Assertions.assertThat(authorityGroup1.getName()).isEqualTo("권한2");
        Assertions.assertThat(authorityGroup1.getDescription()).isEqualTo("권한설명1");
    }

    @Test
    void 수정_이름_비업데이트_성공() {
        //given
        initData();
        //when
        authorityGroupUpdateUseCase.update(AuthorityGroupUpdateDto.Request.builder()
                .id(authorityGroup1.getAuthorityGroupId())
                .description("권한설명2")
                .build());
        //then
        Assertions.assertThat(authorityGroup1.getName()).isEqualTo("권한1");
        Assertions.assertThat(authorityGroup1.getDescription()).isEqualTo("권한설명2");
    }

    @Test
    void 수정_이름중복_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(()->{
            authorityGroupUpdateUseCase.update(AuthorityGroupUpdateDto.Request.builder()
                    .id(authorityGroup1.getAuthorityGroupId())
                    .description("권한설명2")
                    .name("권한1")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME.getMessage());
    }

    @Test
    void 수정_그룹미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(()->{
            authorityGroupUpdateUseCase.update(AuthorityGroupUpdateDto.Request.builder()
                    .id(authorityGroup1.getAuthorityGroupId()+222L)
                    .description("권한설명2")
                    .name("권한2")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP.getMessage());
    }
}