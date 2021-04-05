package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.application.error.TagListeningErrorCode;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningCreateDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningCreateService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final AccountContextService accountContextService;

    public Long create(TagListeningCreateDto.Request request){
        Tag tag = tagJpaRepository.findById(request.getTagId())
                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        Account account = accountContextService.getContextAccount();

        validateDuplicateTagListening(account, tag);

        TagListening tagListening = new TagListening(account,tag);
        tagListeningJpaRepository.save(tagListening);
        return tagListening.getTagListeningId();
    }

    private void validateDuplicateTagListening(Account account, Tag tag) {
        if(tagListeningJpaRepository.findByAccountIdAndTagId(account.getAccountId(), tag.getTagId()).isPresent())
            throw new BusinessException(TagListeningErrorCode.DUPLICATED);
    }

}
