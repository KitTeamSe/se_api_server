package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.notice.application.dto.NoticeCreateDto;
import com.se.apiserver.v1.notice.application.dto.NoticeSendDto;
import com.se.apiserver.v1.noticerecord.application.service.NoticeRecordCreateService;
import com.se.apiserver.v1.noticerecord.application.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    private final NoticeCreateService noticeCreateService;
    private final NoticeRecordCreateService noticeRecordCreateService;

    private final Integer SEND_TIMEOUT = 2;

    @Value("${se-notification-server.send-url}")
    private String NOTIFICATION_SERVER_SEND_URL;

    public void sendPostNotice(List<Tag> tags, Post post){
        List<Long> tagIdList = tags.stream().map(Tag::getTagId).collect(Collectors.toList());
        List<Long> accountList = getTagListeners(tagIdList);
        String title = "게시글이 등록되었습니다";

        String url = "TEST_URL";
        String message = post.getPostContent().getTitle();          //게시글 제목
        sendNotice(accountList, title, message, url);
    }

    public void replySend(List<Tag> tags, Post post) {
        List<Long> tagIdList = tags.stream().map(Tag::getTagId).collect(Collectors.toList());
        List<Long> accountList = getTagListeners(tagIdList);
        String title = "댓글이 등록되었습니다";

        String url = "TEST_URL";
        String message = post.getPostContent().getTitle();          //게시글 제목
        sendNotice(accountList, title, message, url);
    }

    public void managerSend(NoticeSendDto.Request request) {
        List<Long> tagIdList = request.getTagIdList();
        List<Long> accountList = getTagListeners(tagIdList);

        sendNotice(accountList, request.getTitle(), request.getMessage(), request.getUrl());
    }

    public List<Long> getTagListeners(List<Long> tagIdList) {
        Set<Long> accounts = new HashSet<>();

        for (Long tagId: tagIdList) {
            List<TagListening> tagListeningList = tagListeningJpaRepository.findAllByTagId(tagId);
            for (TagListening tl: tagListeningList) {
                accounts.add(tl.getAccount().getAccountId());
            }
        }

        return new ArrayList<>(accounts);
    }

    @Transactional
    private void sendNotice(List<Long> accountIdList, String title, String message, String url) {
        if(accountIdList == null || accountIdList.size() <= 0)
            return;
        //전송
        NoticeSendDto.SendEntity sendEntity = new NoticeSendDto.SendEntity(accountIdList, title, message, url);

        try{
            RestTemplate restTemplate = getTimeoutRestTemplate(SEND_TIMEOUT);
            restTemplate.postForObject(NOTIFICATION_SERVER_SEND_URL, sendEntity, Resource.class);

            //Notice 등록
            NoticeCreateDto.Request noticeCRequest = new NoticeCreateDto.Request(title, message, url);
            Long noticeId = noticeCreateService.save(noticeCRequest);

            //NoticeRecord 등록
            for (Long accountId: accountIdList) {
                NoticeRecordCreateDto.Request noticeRecordCRequest = new NoticeRecordCreateDto.Request(accountId, noticeId);
                noticeRecordCreateService.create(noticeRecordCRequest);
            }
        }
        catch (Exception e){
            // Need logging
            // e.printStackTrace();
            System.out.println("[Notification] Notification server timeout occurred.");
        }

    }

    private RestTemplate getTimeoutRestTemplate(Integer timeout){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout*1000);
        factory.setReadTimeout(timeout*1000);
        return new RestTemplate(factory);
    }
}