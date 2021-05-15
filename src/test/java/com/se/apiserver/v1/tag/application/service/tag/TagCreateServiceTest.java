package com.se.apiserver.v1.tag.application.service.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.application.service.TagCreateService;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TagCreateServiceTest {

    @Autowired
    TagCreateService tagCreateService;
    @Autowired
    TagJpaRepository tagJpaRepository;


    @Test
    void 태그_등록_성공() {
        //given
        //when
        Long id = tagCreateService.create(TagCreateDto.Request.builder().text("새로운 태그").build());
        //then
        Assertions.assertThat(tagJpaRepository.findById(id).get().getText()).isEqualTo("새로운 태그");
    }


    @Test
    void 태그_이름_중복_실패() {
        //given
        Tag tag1 = new Tag("새로운태그");
        //when
        tagJpaRepository.save(tag1);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            tagCreateService.create(TagCreateDto.Request.builder()
            .text("새로운태그").build());
        }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.DUPLICATED_TAG.getMessage());
    }
}