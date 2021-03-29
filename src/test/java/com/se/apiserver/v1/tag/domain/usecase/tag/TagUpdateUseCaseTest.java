package com.se.apiserver.v1.tag.domain.usecase.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagUpdateUseCase;
import com.se.apiserver.v1.tag.infra.dto.tag.TagUpdateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TagUpdateUseCaseTest {

    @Autowired
    TagJpaRepository tagJpaRepository;
    @Autowired
    TagUpdateUseCase tagUpdateUseCase;


    @Test
    void 태그_수정_성공() {
        //given
        Tag tag = Tag.builder()
                .text("태그내용")
                .build();
        tagJpaRepository.save(tag);
        //when
        tagUpdateUseCase.update(TagUpdateDto.Request.builder()
                    .tagId(tag.getTagId())
                    .text("수정내용")
                    .build());
        //then
        Assertions.assertThat(tagJpaRepository.findById(tag.getTagId()).get().getText()).isEqualTo("수정내용");
    }

    @Test
    void 태그_닉네임_중복_실패() {
        //given
        Tag tag1 = Tag.builder()
                .text("태그내용")
                .build();
        tagJpaRepository.save(tag1);
        Tag tag2 = Tag.builder()
                .text("태그내용2")
                .build();
        tagJpaRepository.save(tag2);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            tagUpdateUseCase.update(TagUpdateDto.Request.builder()
                    .tagId(tag1.getTagId())
                    .text("태그내용2")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.DUPLICATED_TAG.getMessage());
    }
}