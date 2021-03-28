package com.se.apiserver.v1.authority.domain.usecase.authoritygroupauthoritymapping;

import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
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
class AuthorityGroupAuthorityMappingCreateUseCaseTest {
    @Autowired
    AuthorityGroupAuthorityMappingCreateUseCase authorityGroupAuthorityMappingCreateUseCase;
    @Autowired
    AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    AuthorityGroup authorityGroup;
    Authority authority;

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
    }

    @Test
    void 등록_성공() {
        //given
        initData();
        //when
        AuthorityGroupAuthorityMappingReadDto.Response response = authorityGroupAuthorityMappingCreateUseCase.create(AuthorityGroupAuthorityMappingCreateDto.Request.builder()
                .authorityId(authority.getAuthorityId())
                .groupId(authorityGroup.getAuthorityGroupId())
                .build());
        //then
        Assertions.assertThat(response.getAuthorityId()).isEqualTo(authority.getAuthorityId());
        Assertions.assertThat(response.getAuthorityIdNameEng()).isEqualTo(authority.getNameEng());
        Assertions.assertThat(response.getAuthorityIdNameKor()).isEqualTo(authority.getNameKor());
        Assertions.assertThat(response.getGroupId()).isEqualTo(authorityGroup.getAuthorityGroupId());
        Assertions.assertThat(response.getGroupName()).isEqualTo(authorityGroup.getName());
    }

    @Test
    void 등록_권한미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            AuthorityGroupAuthorityMappingReadDto.Response response = authorityGroupAuthorityMappingCreateUseCase.create(AuthorityGroupAuthorityMappingCreateDto.Request.builder()
                    .authorityId(authority.getAuthorityId() + 1)
                    .groupId(authorityGroup.getAuthorityGroupId())
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityErrorCode.NO_SUCH_AUTHORITY.getMessage());
    }

    @Test
    void 등록_그룹미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            AuthorityGroupAuthorityMappingReadDto.Response response = authorityGroupAuthorityMappingCreateUseCase.create(AuthorityGroupAuthorityMappingCreateDto.Request.builder()
                    .authorityId(authority.getAuthorityId())
                    .groupId(authorityGroup.getAuthorityGroupId()+1)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP.getMessage());
    }

    @Test
    void 등록_존재하는매핑_실패() {
        //given
        initData();
        //when
        AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = AuthorityGroupAuthorityMapping.builder()
                .authorityGroup(authorityGroup)
                .authority(authority)
                .build();
        authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping);
        //then
        Assertions.assertThatThrownBy(() -> {
            AuthorityGroupAuthorityMappingReadDto.Response response = authorityGroupAuthorityMappingCreateUseCase.create(AuthorityGroupAuthorityMappingCreateDto.Request.builder()
                    .authorityId(authority.getAuthorityId())
                    .groupId(authorityGroup.getAuthorityGroupId())
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupAuthorityMappingErrorCode.ALREADY_EXIST.getMessage());
    }
}