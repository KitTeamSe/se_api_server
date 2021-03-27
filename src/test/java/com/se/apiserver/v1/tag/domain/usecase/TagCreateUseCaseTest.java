package com.se.apiserver.v1.tag.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.dto.TagCreateDto;
import com.se.apiserver.v1.tag.infra.dto.TagReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagCreateUseCaseTest {

    @Autowired
    TagCreateUseCase tagCreateUseCase;
    @Autowired
    TagJpaRepository tagJpaRepository;


    @Test
    void 태그_등록_성공() {
        //given
        //when
        TagReadDto.Response tag = tagCreateUseCase.create(TagCreateDto.Request.builder()
                .text("새로운 태그").build());
        //then
        Assertions.assertThat(tagJpaRepository.findById(tag.getTagId()).get().getText()).isEqualTo("새로운 태그");
    }


    @Test
    void 태그_이름_중복_실패() {
        //given
        Tag tag1 = Tag.builder()
                .text("새로운태그")
                .build();
        //when
        tagJpaRepository.save(tag1);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            tagCreateUseCase.create(TagCreateDto.Request.builder()
            .text("새로운태그").build());
        }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.DUPLICATED_TAG.getMessage());
    }
}