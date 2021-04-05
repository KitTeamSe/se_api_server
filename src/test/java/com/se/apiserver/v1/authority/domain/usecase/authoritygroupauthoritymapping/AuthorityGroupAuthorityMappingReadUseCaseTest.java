package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityGroupAuthorityMappingReadUseCaseTest {
    @Autowired
    AuthorityGroupAuthorityMappingReadUseCase authorityGroupAuthorityMappingReadUseCase;
    @Autowired
    AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    AuthorityGroup authorityGroup;
    Authority authority1;
    Authority authority2;
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping1;
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping2;

    void initData(){
        authorityGroup = new AuthorityGroup("그룹1", "그룹1 설명", AuthorityGroupType.NORMAL);
        authorityGroupJpaRepository.save(authorityGroup);

        authority1 = new Authority("auth1", "권한1");
        authorityJpaRepository.save(authority1);

        authority2 = new Authority("auth2", "권한2");
        authorityJpaRepository.save(authority2);

        authorityGroupAuthorityMapping1 = new AuthorityGroupAuthorityMapping(authority1,authorityGroup);
        authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping1);

        authorityGroupAuthorityMapping2 = new AuthorityGroupAuthorityMapping(authority2,authorityGroup);
        authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping2);
    }

    @Test
    void 조회_성공() {
        //given
        initData();
        //when
        AuthorityGroupAuthorityMappingReadDto.Response read = authorityGroupAuthorityMappingReadUseCase
                .read(authorityGroupAuthorityMapping1.getAuthorityGroupAuthorityMappingId());
        //then
        Assertions.assertThat(read.getGroupName()).isEqualTo(authorityGroup.getName());
        Assertions.assertThat(read.getGroupId()).isEqualTo(authorityGroup.getAuthorityGroupId());
        Assertions.assertThat(read.getAuthorityIdNameKor()).isEqualTo(authority1.getNameKor());
        Assertions.assertThat(read.getAuthorityIdNameEng()).isEqualTo(authority1.getNameEng());
        Assertions.assertThat(read.getAuthorityId()).isEqualTo(authority1.getAuthorityId());
    }

    @Test
    void 조회_미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(()->{
            AuthorityGroupAuthorityMappingReadDto.Response read = authorityGroupAuthorityMappingReadUseCase
                    .read(authorityGroupAuthorityMapping1.getAuthorityGroupAuthorityMappingId()+10);
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING.getMessage());
    }

    @Test
    void 목록_조회_성공() {
        //given
        initData();
        //when
        PageImpl read = authorityGroupAuthorityMappingReadUseCase
                .readAll(PageRequest.builder()
                .page(1)
                .size(10)
                .direction(Sort.Direction.ASC)
                .build().of());
        //then
        Assertions.assertThat(read.getTotalElements()).isEqualTo(authorityGroupAuthorityMappingJpaRepository.count());
    }
}