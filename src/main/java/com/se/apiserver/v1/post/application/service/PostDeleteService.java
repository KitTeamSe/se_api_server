package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostDeleteService {

  private final PostJpaRepository postJpaRepository;
  private final AccountContextService accountContextService;
  private final PasswordEncoder passwordEncoder;

  public PostDeleteService(
      PostJpaRepository postJpaRepository,
      AccountContextService accountContextService,
      PasswordEncoder passwordEncoder) {
    this.postJpaRepository = postJpaRepository;
    this.accountContextService = accountContextService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public boolean delete(Long postId) {
    Account account = accountContextService.getContextAccount();
    Set<String> authorities = accountContextService.getContextAuthorities();
    Post post = postJpaRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    post.delete(account, authorities);
    postJpaRepository.save(post);
    return true;
  }

  @Transactional
  public boolean delete(PostDeleteDto.AnonymousPostDeleteRequest request) {
    Post post = postJpaRepository.findById(request.getPostId())
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    String inputPassword = request.getAnonymousPassword();
    if (!passwordEncoder.matches(inputPassword, post.getAnonymousPassword())) {
      throw new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT);
    }
    post.delete();
    postJpaRepository.save(post);
    return true;
  }
}