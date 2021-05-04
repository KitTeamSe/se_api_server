package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDeleteService {

    private final PostJpaRepository postJpaRepository;

    @Transactional
    public boolean delete(Long postId){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        postJpaRepository.delete(post);
        return true;
    }
}
