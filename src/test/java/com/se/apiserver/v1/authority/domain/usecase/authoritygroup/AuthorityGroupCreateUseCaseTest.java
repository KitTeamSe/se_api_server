package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityGroupCreateUseCaseTest {

    @Autowired
    AuthorityGroupCreateUseCase authorityGroupCreateUseCase;

    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;

    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    Authority authority;

    void createData(){
        authority = new Authority("newAuth", "새로운권한");
        authorityJpaRepository.save(authority);
    }


    @Test
    void 권한그룹_생성_성공() {
        createData();
        //given
        AuthorityGroupReadDto.Response response = authorityGroupCreateUseCase.create(AuthorityGroupCreateDto.Request.builder()
                .name("권한그룹")
                .description("권한그룹 설명")
                .build());
        //when
        AuthorityGroup authorityGroup = authorityGroupJpaRepository.findById(response.getAuthorityGroupId()).get();
        //then
        Assertions.assertThat(authorityGroup.getAuthorityGroupId()).isEqualTo(response.getAuthorityGroupId());
        Assertions.assertThat(authorityGroup.getDescription()).isEqualTo("권한그룹 설명");
        Assertions.assertThat(authorityGroup.getName()).isEqualTo("권한그룹");

        Assertions.assertThat(response.getDescription()).isEqualTo("권한그룹 설명");
        Assertions.assertThat(response.getName()).isEqualTo("권한그룹");
    }

    @Test
    void 권한그룹_이름중복_실패() {
        //given
        AuthorityGroup authorityGroup = new AuthorityGroup("권한그룹","권한 그룹 설명", AuthorityGroupType.ANONYMOUS);
        authorityGroupJpaRepository.save(authorityGroup);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            authorityGroupCreateUseCase.create(AuthorityGroupCreateDto.Request.builder()
                    .name("권한그룹")
                    .description("권한그룹 설명")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME.getMessage());
    }
}