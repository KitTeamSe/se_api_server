package com.se.apiserver.v1.tag.domain.usecase.taglistening;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.entity.TagListening;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.error.TagListeningErrorCode;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningCreateDto;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.tag.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagListeningCreateUseCase {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final AccountDetailService accountDetailService;

    public Long create(TagListeningCreateDto.Request request){
        Tag tag = tagJpaRepository.findById(request.getTagId())
                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));
        Account account = accountDetailService.getContextAccount();

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
