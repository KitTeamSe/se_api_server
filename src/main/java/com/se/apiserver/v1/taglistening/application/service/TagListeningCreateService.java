package com.se.apiserver.v1.taglistening.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.common.domain.exception.PreconditionFailedException;
import com.se.apiserver.v1.common.domain.exception.UnauthenticatedException;
import com.se.apiserver.v1.common.domain.exception.UniqueValueAlreadyExistsException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningCreateDto;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.Tags;

@Service
@Transactional(readOnly = true)
public class TagListeningCreateService {
    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final AccountContextService accountContextService;

    public TagListeningCreateService(
        TagListeningJpaRepository tagListeningJpaRepository,
        TagJpaRepository tagJpaRepository,
        AccountContextService accountContextService) {
        this.tagListeningJpaRepository = tagListeningJpaRepository;
        this.tagJpaRepository = tagJpaRepository;
        this.accountContextService = accountContextService;
    }

    @Transactional
    public Long create(TagListeningCreateDto.Request request){
        Tag tag = tagJpaRepository.findById(request.getTagId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 태그"));

        if (!accountContextService.isSignIn()) {
            throw new UnauthenticatedException("수신 태그는 로그인 후 등록 가능합니다");
        }

        Account account = accountContextService.getContextAccount();

        checkPossibleToCreateTagListening(account.getAccountId());
        validateDuplicateTagListening(account, tag);

        TagListening tagListening = new TagListening(account,tag);
        tagListeningJpaRepository.save(tagListening);
        return tagListening.getTagListeningId();
    }

    private void validateDuplicateTagListening(Account account, Tag tag) {
        if(tagListeningJpaRepository.findByAccountIdAndTagId(account.getAccountId(), tag.getTagId()).isPresent())
            throw new UniqueValueAlreadyExistsException("중복된 내용 삽입");
    }

    private void checkPossibleToCreateTagListening (Long accountId) {
        List<TagListening> tagListenings = tagListeningJpaRepository.findAllByAccountId(accountId);

        if (tagListenings.size() >= TagListening.MAX_TAG_LISTENING_CAPACITY) {
            throw new PreconditionFailedException("등록 가능한 최대 수신 태그 개수를 초과하였습니다");
        }
    }
}
