package com.se.apiserver.v1.attach.domain.usecase;


import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsDeleted;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AttachCreateUseCaseTest {
  @Autowired
  AttachJpaRepository attachJpaRepository;
  @Autowired
  AttachCreateUseCase attachCreateUseCase;
  @Autowired
  PostJpaRepository postJpaRepository;
  @Autowired
  ReplyJpaRepository replyJpaRepository;

  Post post;

  Reply reply;

  private void initData() {
    post = Post.builder()
        .anonymous(
            Anonymous.builder()
                .anonymousNickname("익명1")
                .anonymousPassword("testest")
                .build()
        )
        .isDeleted(PostIsDeleted.NORMAL)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .text("test")
        .title("title...")
        .numReply(0)
        .views(0)
        .build();

    reply = Reply.
  }

  @Test
  void 생성_성공() {
    initData();
  }

}