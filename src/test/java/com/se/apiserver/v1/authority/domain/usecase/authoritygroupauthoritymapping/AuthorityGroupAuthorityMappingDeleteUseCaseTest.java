package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class AuthorityGroupAuthorityMappingDeleteUseCaseTest {
    @Autowired
    AuthorityGroupAuthorityMappingDeleteUseCase authorityGroupAuthorityMappingDeleteUseCase;
    @Autowired
    AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    AuthorityGroup authorityGroup;
    Authority authority;
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping;

    void initData(){
        authorityGroup = AuthorityGroup.builder()
                .name("그룹1")
                .description("그룹1 설명")
                .type(AuthorityGroupType.NORMAL)
                .build();
        authorityGroupJpaRepository.save(authorityGroup);

        authority = Authority.builder()
                .nameEng("auth1")
                .nameKor("권한1")
                .build();
        authorityJpaRepository.save(authority);
        
        authorityGroupAuthorityMapping = AuthorityGroupAuthorityMapping.builder()
                .authority(authority)
                .authorityGroup(authorityGroup)
                .build();
        authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping);
    }

    @Test
    void 삭제_성공() {
        //given
        initData();
        //when
        Long id = authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId();
        authorityGroupAuthorityMappingDeleteUseCase.delete(authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId());
        //then
        Assertions.assertThat(authorityGroupAuthorityMappingJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }
    @Test
    void 삭제_미존재_실패() {
        //given
        initData();
        //when
        Long id = authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId();
        //then
        Assertions.assertThatThrownBy(()->{
            authorityGroupAuthorityMappingDeleteUseCase.delete(authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId()+1);
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING.getMessage());
    }
}