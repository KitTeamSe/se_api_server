package com.se.apiserver.v1.notice.domain.service;

import com.se.apiserver.v1.account.domain.entity.AccountReceiveTagMapping;
import com.se.apiserver.v1.account.infra.repository.AccountReceiveTagMappingJpaRepository;
import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import com.se.apiserver.v1.notice.infra.dto.NoticeSendDto;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordCreateService;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeSendService {

    private final AccountReceiveTagMappingJpaRepository accountReceiveTagMappingJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final NoticeCreateService noticeCreateUseCase;
    private final NoticeRecordCreateService noticeRecordCreateUseCase;
    String NOTICESEND_URL = "localhost:8088/notice/multi-message";

    public void postSend(NoticeSendDto.Request request){
        List<Long> tagIdList = request.getTagIdList();
        List<Long> accountList = createAccountList(tagIdList);
        String title = "게시글이 등록되었습니다";
        String message = postJpaRepository.findById(request.getPostId()).get().getPostContent().getTitle(); //게시글 제목

        send(accountList, title, message, request.getUrl());
    }

    public void replySend(NoticeSendDto.Request request) {
        List<Long> tagIdList = request.getTagIdList();
        List<Long> accountList = createAccountList(tagIdList);
        String title = "댓글이 등록되었습니다";
        String message = postJpaRepository.findById(request.getPostId()).get().getPostContent().getTitle(); //게시글 제목

        send(accountList, title, message, request.getUrl());
    }

    public void managerSend(NoticeSendDto.Request request) {
        List<Long> tagIdList = request.getTagIdList();
        List<Long> accountList = createAccountList(tagIdList);

        send(accountList, request.getTitle(), request.getMessage(), request.getUrl());
    }

    public List<Long> createAccountList(List<Long> tagIdList) {
        List<Long> accountList = new ArrayList<>();

        List<AccountReceiveTagMapping> accountTagList = accountReceiveTagMappingJpaRepository.findAccountReceiveTagMappingsByTag_TagIdIn(tagIdList);
        for (AccountReceiveTagMapping a: accountTagList
             ) {
            accountList.add(a.getAccount().getAccountId());
        }
        return accountList;
    }

    public void send(List<Long> accountList, String title, String message, String url) {
        //전송
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(NOTICESEND_URL)
                .queryParam("accountIdList", accountList)
                .queryParam("title", title)
                .queryParam("message", message)
                .queryParam("url", url)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, null , Resource.class);

        //Notice 등록
        NoticeCreateDto.Request noticeCRequest = new NoticeCreateDto.Request(title, message, url);
        Long noticeId = noticeCreateUseCase.save(noticeCRequest);

        //NoticeRecord 등록
        for (Long accountId: accountList
             ) {
            NoticeRecordCreateDto.Request noticeRecordCRequest = new NoticeRecordCreateDto.Request(accountId, noticeId);
            noticeRecordCreateUseCase.create(noticeRecordCRequest);
        }
    }
}

