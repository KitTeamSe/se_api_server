package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDeleteService {

    private final PostJpaRepository postJpaRepository;
    private final MultipartFileDeleteService multipartFileDeleteService;
    @Transactional
    public boolean delete(Long postId){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        List<Attach> attachList = post.getAttaches();
        String[] attachSaveNameList = attachList.stream()
                .map(attach -> {
                    return attach.getSaveName();
                })
                .toArray(String[]::new);
        multipartFileDeleteService.delete(attachSaveNameList);
        postJpaRepository.delete(post);
        return true;
    }
}
