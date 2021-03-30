package com.se.apiserver.v1.tag.domain.usecase.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagReadUseCase;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TagReadUseCaseTest {
    @Autowired
    TagReadUseCase tagReadUseCase;
    @Autowired
    TagJpaRepository tagJpaRepository;

    @Test
    void 태그_조회_성공() {
        //given
        Tag tag = Tag.builder()
                .text("태그명")
                .build();
        tagJpaRepository.save(tag);
        //when
        //then
        Assertions.assertThat(tagReadUseCase.readById(tag.getTagId()).getText()).isEqualTo(tag.getText());
    }

    @Test
    void 태그_매칭_성공() {
        //given
        Tag tag = Tag.builder()
                .text("태그명")
                .build();
        tagJpaRepository.save(tag);
        Tag tag2 = Tag.builder()
                .text("태그명2")
                .build();
        tagJpaRepository.save(tag2);
        //when
        //then
        Assertions.assertThat(tagReadUseCase.readMatchText("태그").size()).isEqualTo(2);
    }

    @Test
    void 태그_전체검색_성공() {
        //given
        Tag tag = Tag.builder()
                .text("태그명")
                .build();
        tagJpaRepository.save(tag);
        Tag tag2 = Tag.builder()
                .text("태그명2")
                .build();
        tagJpaRepository.save(tag2);
        //when
        //then
        PageImpl responses = tagReadUseCase.readAll(PageRequest.builder()
                .size(10)
                .direction(Sort.Direction.ASC)
                .page(1)
                .build().of());
        Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 태그_해당태그없음_실패() {
        //given
        Tag tag = Tag.builder()
                .text("태그명")
                .build();
        tagJpaRepository.save(tag);
        //when
        //then
        Assertions.assertThatThrownBy(()->{
            tagReadUseCase.readById(tag.getTagId()+1);
        }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.NO_SUCH_TAG.getMessage());
    }
}