package com.se.apiserver.v1.notice.domain.service;

import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import com.se.apiserver.v1.notice.infra.dto.NoticeSendDto;
import com.se.apiserver.v1.notice.infra.dto.SendEntity;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordCreateService;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeSendService {

    private final TagListeningJpaRepository tagListeningJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final NoticeCreateService noticeCreateService;
    private final NoticeRecordCreateService noticeRecordCreateService;

    @Value("${se-notification-server.send-url}")
    private String SEND_URL;

    public void postSend(NoticeSendDto.Request request){
        List<Long> tagIdList = request.getTagIdList();
        List<Long> accountList = createAccountList(tagIdList);
        String title = "게시글이 등록되었습니다";
        String message = postJpaRepository.findById(request.getPostId()).get().getPostContent().getTitle();//게시글 제목

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

        //List<TagListening> tagListeningList = tagListeningJpaRepository.findTagListeningsByTag_TagId(tagIdList);

        for (Long tagId: tagIdList
             ) {
            List<TagListening> tagListeningList = tagListeningJpaRepository.findAllByTagId(tagId);
            for (TagListening tl: tagListeningList
                 ) {
                accountList.add(tl.getAccount().getAccountId());
            }
        }

        return accountList;
    }

    @Transactional
    public void send(List<Long> accountList, String title, String message, String url) {
        //전송
        SendEntity sendEntity = SendEntity.builder()
                .accountIdList(accountList)
                .title(title)
                .message(message)
                .url(url)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(SEND_URL, sendEntity, Resource.class);

        //Notice 등록
        NoticeCreateDto.Request noticeCRequest = new NoticeCreateDto.Request(title, message, url);
        Long noticeId = noticeCreateService.save(noticeCRequest);

        //NoticeRecord 등록
        for (Long accountId: accountList
        ) {
            NoticeRecordCreateDto.Request noticeRecordCRequest = new NoticeRecordCreateDto.Request(accountId, noticeId);
            noticeRecordCreateService.create(noticeRecordCRequest);
        }
    }
}