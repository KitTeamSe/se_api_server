package com.se.apiserver.v1.tag.application.service;

import com.se.apiserver.v1.common.domain.exception.UniqueValueAlreadyExistsException;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagCreateService {

  private final TagJpaRepository tagJpaRepository;

  public TagCreateService(TagJpaRepository tagJpaRepository) {
    this.tagJpaRepository = tagJpaRepository;
  }

  @Transactional
  public Long create(TagCreateDto.Request request) {
    Tag tag = new Tag(request.getText());

      if (tagJpaRepository.findByText(tag.getText()).isPresent()) {
          throw new UniqueValueAlreadyExistsException("중복된 태그명입니다");
      }

    tagJpaRepository.save(tag);
    return tag.getTagId();
  }
}
