package com.se.apiserver.v1.tag.application.service.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.application.service.TagUpdateService;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.application.dto.TagUpdateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TagUpdateServiceTest {

    @Autowired
    TagJpaRepository tagJpaRepository;
    @Autowired
    TagUpdateService tagUpdateService;


    @Test
    void 태그_수정_성공() {
        //given
        Tag tag = new Tag("태그 내용");
        tagJpaRepository.save(tag);
        //when
        tagUpdateService.update(TagUpdateDto.Request.builder()
                    .tagId(tag.getTagId())
                    .text("수정내용")
                    .build());
        //then
        Assertions.assertThat(tagJpaRepository.findById(tag.getTagId()).get().getText()).isEqualTo("수정내용");
    }

    @Test
    void 태그_닉네임_중복_실패() {
        //given
        Tag tag1 = new Tag("태그 내용");
        tagJpaRepository.save(tag1);
        Tag tag2 = new Tag("태그 내용2");
        tagJpaRepository.save(tag2);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            tagUpdateService.update(TagUpdateDto.Request.builder()
                    .tagId(tag1.getTagId())
                    .text("태그 내용2")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.DUPLICATED_TAG.getMessage());
    }
}