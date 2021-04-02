package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDeleteUseCase {

    private final PostJpaRepository postJpaRepository;

    public boolean delete(Long postId){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        postJpaRepository.delete(post);
        return true;
    }
}
