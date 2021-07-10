package com.se.apiserver.v1.authority.application.service.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
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
class AuthorityGroupReadServiceTest {
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AuthorityGroupReadService authorityGroupReadService;

    AuthorityGroup authorityGroup1;
    AuthorityGroup authorityGroup2;


    @Test
    void 조회_성공() {
        //given
        initData();
        //when
        AuthorityGroupReadDto.Response authorityGroup = authorityGroupReadService.read(authorityGroup1.getAuthorityGroupId());
        //then

        Assertions.assertThat(authorityGroup.getAuthorityGroupId()).isEqualTo(authorityGroup1.getAuthorityGroupId());
        Assertions.assertThat(authorityGroup.getName()).isEqualTo(authorityGroup1.getName());
        Assertions.assertThat(authorityGroup.getType()).isEqualTo(authorityGroup1.getType());
    }

    @Test
    void 목록_조회_성공() {
        //given
        Long num = authorityGroupJpaRepository.count();
        initData();
        //when
        PageImpl page = authorityGroupReadService.readAll(PageRequest.builder()
        .page(1)
        .size(10)
        .direction(Sort.Direction.ASC)
        .build().of());
        //then

        Assertions.assertThat(page.getTotalElements()).isEqualTo(2+num);
    }

    private void initData() {
        authorityGroup1 = new AuthorityGroup("권한1", "권한설명1", AuthorityGroupType.NORMAL);
        authorityGroup2 = new AuthorityGroup("권한2", "권한설명2", AuthorityGroupType.NORMAL);

        authorityGroupJpaRepository.save(authorityGroup1);
        authorityGroupJpaRepository.save(authorityGroup2);
    }
}