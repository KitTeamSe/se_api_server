package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
class PostCreateUseCaseTest {
    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    PostCreateUseCase postCreateUseCase;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;
    @Autowired
    BoardJpaRepository boardJpaRepository;
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    TagJpaRepository tagJpaRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AttachJpaRepository attachJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    Account user;
    Account admin;
    Attach attach;

    Board board;
    Tag tag;

    void initData(){
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        user = Account.builder()
                .idString("test")
                .email("test@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20003156")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();

        admin = Account.builder()
                .idString("test2")
                .email("test2@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd2")
                .phoneNumber("555555555552")
                .studentId("20003152")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd2")
                .build();

        accountJpaRepository.save(admin);
        accountJpaRepository.save(user);

        board = new Board("freeboard", "자유게시판");
        boardJpaRepository.save(board);

        tag = new Tag("태그1");
        tagJpaRepository.save(tag);

        attach = new Attach("testurl", "testfile");
        attachJpaRepository.save(attach);
    }


    @Test
    void 게시글_일반회원_등록_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));
        //when
        Long id = postCreateUseCase.create(PostCreateDto.Request.builder()
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NORMAL)
                .isSecret(PostIsSecret.SECRET)
                .postContent(new PostContent("제목...", "내용..."))
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                        .attachId(attach.getAttachId())
                        .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                        .tagId(tag.getTagId())
                        .build()))
                .build());
        Post post = postJpaRepository.findById(id).get();
        //then
        Assertions.assertThat(post.getAccount().getAccountId()).isEqualTo(user.getAccountId());
        Assertions.assertThat(post.getBoard().getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(post.getIsNotice()).isEqualTo(PostIsNotice.NORMAL);
        Assertions.assertThat(post.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(post.getPostContent().getText()).isEqualTo("내용...");
        Assertions.assertThat(post.getPostContent().getTitle()).isEqualTo("제목...");
        Assertions.assertThat(post.getViews()).isEqualTo(0);
        Assertions.assertThat(post.getTags().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(post.getTags().get(0).getTag().getText()).isEqualTo("태그1");
    }

    @Test
    void 게시글_관리자_등록_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("MENU_MANAGE"))));
        //when
        Long id = postCreateUseCase.create(PostCreateDto.Request.builder()
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NOTICE)
                .isSecret(PostIsSecret.SECRET)
                .postContent(new PostContent("제목...", "내용..."))
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                        .attachId(attach.getAttachId())
                        .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                        .tagId(tag.getTagId())
                        .build()))
                .build());
        Post post = postJpaRepository.findById(id).get();
        //then
        Assertions.assertThat(post.getAccount().getAccountId()).isEqualTo(admin.getAccountId());
        Assertions.assertThat(post.getBoard().getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(post.getIsNotice()).isEqualTo(PostIsNotice.NOTICE);
        Assertions.assertThat(post.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(post.getPostContent().getText()).isEqualTo("내용...");
        Assertions.assertThat(post.getPostContent().getTitle()).isEqualTo("제목...");
        Assertions.assertThat(post.getViews()).isEqualTo(0);
        Assertions.assertThat(post.getTags().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(post.getTags().get(0).getTag().getText()).isEqualTo("태그1");
    }

    @Test
    void 게시글_익명_등록_성공() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(0,
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

        //when

        Long id = postCreateUseCase.create(PostCreateDto.Request.builder()
                .anonymous(new Anonymous("익명1","test2"))
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NORMAL)
                .isSecret(PostIsSecret.SECRET)
                .postContent(new PostContent("제목...", "내용..."))
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                        .attachId(attach.getAttachId())
                        .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                        .tagId(tag.getTagId())
                        .build()))
                .build());
        Post post = postJpaRepository.findById(id).get();
        //then
        Assertions.assertThat(post.getAnonymous().getAnonymousNickname()).isEqualTo("익명1");
        Assertions.assertThat(post.getBoard().getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(post.getIsNotice()).isEqualTo(PostIsNotice.NORMAL);
        Assertions.assertThat(post.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(post.getPostContent().getText()).isEqualTo("내용...");
        Assertions.assertThat(post.getPostContent().getTitle()).isEqualTo("제목...");
        Assertions.assertThat(post.getViews()).isEqualTo(0);
        Assertions.assertThat(post.getTags().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(post.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(post.getTags().get(0).getTag().getText()).isEqualTo("태그1");
    }

    @Test
    void 게시글_일반사용자_공지등록_실패() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            postCreateUseCase.create(PostCreateDto.Request.builder()
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .postContent(new PostContent("제목...", "내용..."))
                    .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                            .attachId(attach.getAttachId())
                            .build()))
                    .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                            .tagId(tag.getTagId())
                            .build()))
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(PostErrorCode.ONLY_ADMIN_SET_NOTICE.getMessage());
    }

    @Test
    void 게시글_익명사용자_공지등록_실패() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(0,
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            postCreateUseCase.create(PostCreateDto.Request.builder()
                    .anonymous(new Anonymous("익명1","test2"))
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .postContent(new PostContent("제목...", "내용..."))
                    .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                            .attachId(attach.getAttachId())
                            .build()))
                    .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                            .tagId(tag.getTagId())
                            .build()))
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(PostErrorCode.ONLY_ADMIN_SET_NOTICE.getMessage());
    }

    @Test
    void 게시글_익명사용자_입력값오류_실패() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(0,
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            postCreateUseCase.create(PostCreateDto.Request.builder()
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .postContent(new PostContent("제목...", "내용..."))
                    .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                            .attachId(attach.getAttachId())
                            .build()))
                    .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                            .tagId(tag.getTagId())
                            .build()))
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(PostErrorCode.INVALID_INPUT.getMessage());
    }
}