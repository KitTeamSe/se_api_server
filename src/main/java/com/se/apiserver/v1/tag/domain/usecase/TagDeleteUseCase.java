package com.se.apiserver.v1.tag.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagDeleteUseCase {

    private final TagJpaRepository tagJpaRepository;

    public boolean delete(Long id) {
        Tag tag = tagJpaRepository.findById(id).orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        tagJpaRepository.delete(tag);
        return true;
    }
}
