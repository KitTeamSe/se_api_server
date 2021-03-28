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
    private final AccountJpaRepository accountJpaRepository;
    private final AccountDetailService accountDetailService;

    public TagListeningReadDto.Response create(TagListeningCreateDto.Request request){

        Tag tag = tagJpaRepository.findById(request.getTagId())
                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG));

        Account account = accountJpaRepository.findById(request.getAccountId())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

        if(!accountDetailService.isOwner(account) && !accountDetailService.hasAuthority("TAG_MANAGE"))
            throw new AccessDeniedException("권한 없음");

        if(tagListeningJpaRepository.findByAccountIdAndTagId(request.getAccountId(), request.getTagId()).isPresent())
            throw new BusinessException(TagListeningErrorCode.DUPLICATED);

        TagListening tagListening = TagListening.builder()
                .tag(tag)
                .account(account)
                .build();
        tagListeningJpaRepository.save(tagListening);
        return TagListeningReadDto.Response.fromEntity(tagListening);
    }

}
