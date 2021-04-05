package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthorityGroupDeleteServiceTest {
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityGroupDeleteService authorityGroupDeleteService;
    AuthorityGroup authorityGroup;
    void createData(AuthorityGroupType type){
        authorityGroup = new AuthorityGroup("권한그룹","권한 그룹 설명", type);
        authorityGroupJpaRepository.save(authorityGroup);
    }
    @Test
    void 권한그룹_삭제_성공() {
        //given
        createData(AuthorityGroupType.NORMAL);
        Long id  = authorityGroup.getAuthorityGroupId();
        //when
        authorityGroupDeleteService.delete(authorityGroup.getAuthorityGroupId());
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
            authorityGroupDeleteService.delete(authorityGroup.getAuthorityGroupId());
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
            authorityGroupDeleteService.delete(authorityGroup.getAuthorityGroupId());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP.getMessage());
    }
}