package com.se.apiserver.v1.authority.domain.usecase.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthorityGroupDeleteUseCaseTest {
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityGroupDeleteUseCase authorityGroupDeleteUseCase;
    AuthorityGroup authorityGroup;
    void createData(AuthorityGroupType type){
        authorityGroup = AuthorityGroup.builder()
                .name("권한그룹")
                .description("권한그룹설명")
                .type(type)
                .build();
        authorityGroupJpaRepository.save(authorityGroup);
    }
    @Test
    void 권한그룹_삭제_성공() {
        //given
        createData(AuthorityGroupType.NORMAL);
        Long id  = authorityGroup.getAuthorityGroupId();
        //when
        authorityGroupDeleteUseCase.delete(authorityGroup.getAuthorityGroupId());
        //then
        Assertions.assertThat(authorityGroupJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }

    //TODO sql 스크립트 의존
    @Test
    void 권한그룹_익명그룹삭제_실패() {
        //given
        createData(AuthorityGroupType.ANONYMOUS);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            authorityGroupDeleteUseCase.delete(authorityGroup.getAuthorityGroupId());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.CAN_NOT_DELETE_ANONYMOUS_GROUP.getMessage());
    }

    //TODO sql 스크립트 의존
    @Test
    void 권한그룹_기본그룹삭제_실패() {
        //given
        //given
        createData(AuthorityGroupType.DEFAULT);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            authorityGroupDeleteUseCase.delete(authorityGroup.getAuthorityGroupId());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP.getMessage());
    }
}