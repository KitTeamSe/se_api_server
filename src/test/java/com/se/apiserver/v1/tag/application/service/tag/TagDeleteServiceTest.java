package com.se.apiserver.v1.tag.application.service.tag;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.application.service.TagDeleteService;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TagDeleteServiceTest {
    @Autowired
    TagDeleteService tagDeleteService;
    @Autowired
    TagJpaRepository tagJpaRepository;

    @Test
    void 태그_삭제_성공() {
        //given
        Tag tag = new Tag("태그 내용");
        tagJpaRepository.save(tag);
        Long id = tag.getTagId();
        //when
        tagDeleteService.delete(tag.getTagId());
        //then
        Assertions.assertThat(tagJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }

    @Test
    void 태그_해당태그없음_실패() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() ->{
                tagDeleteService.delete(100L);
                }).isInstanceOf(BusinessException.class).hasMessage(TagErrorCode.NO_SUCH_TAG.getMessage());

    }
}
