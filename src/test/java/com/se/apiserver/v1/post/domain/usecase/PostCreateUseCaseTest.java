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
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
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
    Menu menu;
    Authority authority;

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



        menu = Menu.builder()
                .menuType(MenuType.BOARD)
                .menuOrder(1)
                .description("일반메뉴")
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .url("test")
                .build();
        menuJpaRepository.save(menu);

        authority = Authority.builder()
            .nameEng("BOARD_freeeboard_ACCESS")
            .nameKor("자유게시판 접근")
            .build();
        authority.updateMenu(menu);
        authorityJpaRepository.save(authority);


        board = Board.builder()
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .menu(menu)
                .build();
        boardJpaRepository.save(board);

        tag = Tag.builder()
                .text("태그1")
                .build();
        tagJpaRepository.save(tag);

        attach = Attach.builder()
            .downloadUrl("testurl")
            .fileName("testfile")
            .build();
        attachJpaRepository.save(attach);
    }


    @Test
    void 게시글_일반회원_등록_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when
        PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                .accountId(user.getAccountId())
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NORMAL)
                .isSecret(PostIsSecret.SECRET)
                .text("내용...")
                .title("제목...")
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                        .attachId(attach.getAttachId())
                        .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                        .tagId(tag.getTagId())
                        .build()))
                .build());
        //then
        Assertions.assertThat(res.getAccountId()).isEqualTo(user.getAccountId());
        Assertions.assertThat(res.getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(res.getIsNotice()).isEqualTo(PostIsNotice.NORMAL);
        Assertions.assertThat(res.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(res.getText()).isEqualTo("내용...");
        Assertions.assertThat(res.getTitle()).isEqualTo("제목...");
        Assertions.assertThat(res.getViews()).isEqualTo(0);
        Assertions.assertThat(res.getTags().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(res.getTags().get(0).getTag()).isEqualTo("태그1");
    }

    @Test
    void 게시글_관리자_등록_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("MENU_MANAGE"))));
        //when
        PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                .accountId(admin.getAccountId())
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NOTICE)
                .isSecret(PostIsSecret.SECRET)
                .text("내용...")
                .title("제목...")
            .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                .attachId(attach.getAttachId())
                .build()))
            .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                .tagId(tag.getTagId())
                .build()))
                .build());
        //then
        Assertions.assertThat(res.getAccountId()).isEqualTo(admin.getAccountId());
        Assertions.assertThat(res.getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(res.getIsNotice()).isEqualTo(PostIsNotice.NOTICE);
        Assertions.assertThat(res.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(res.getText()).isEqualTo("내용...");
        Assertions.assertThat(res.getTitle()).isEqualTo("제목...");
        Assertions.assertThat(res.getViews()).isEqualTo(0);
        Assertions.assertThat(res.getTags().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(res.getTags().get(0).getTag()).isEqualTo("태그1");
    }

    @Test
    void 게시글_익명_등록_성공() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(0,
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when

        PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                .anonymousNickname("익명1")
                .anonymousPassword("password")
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NORMAL)
                .isSecret(PostIsSecret.SECRET)
                .text("내용...")
                .title("제목...")
            .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                .attachId(attach.getAttachId())
                .build()))
            .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                .tagId(tag.getTagId())
                .build()))
                .build());
        //then
        Assertions.assertThat(res.getAccountId()).isEqualTo(null);
        Assertions.assertThat(res.getAnonymousNickname()).isEqualTo("익명1");
        Assertions.assertThat(res.getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(res.getIsNotice()).isEqualTo(PostIsNotice.NORMAL);
        Assertions.assertThat(res.getIsSecret()).isEqualTo(PostIsSecret.SECRET);
        Assertions.assertThat(res.getText()).isEqualTo("내용...");
        Assertions.assertThat(res.getTitle()).isEqualTo("제목...");
        Assertions.assertThat(res.getViews()).isEqualTo(0);
        Assertions.assertThat(res.getTags().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().size()).isEqualTo(1);
        Assertions.assertThat(res.getAttaches().get(0).getFileName()).isEqualTo("testfile");
        Assertions.assertThat(res.getTags().get(0).getTag()).isEqualTo("태그1");
    }

    @Test
    void 게시글_입력오류_실패() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                    .accountId(user.getAccountId())
                    .anonymousNickname("익명1")
                    .anonymousPassword("password")
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NORMAL)
                    .isSecret(PostIsSecret.SECRET)
                    .text("내용...")
                    .title("제목...")
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                    .attachId(attach.getAttachId())
                    .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                    .tagId(tag.getTagId())
                    .build()))
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(PostErrorCode.INVALID_INPUT.getMessage());
    }
    @Test
    void 게시글_요청계정과_다른계정으로_등록_실패() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                    .accountId(user.getAccountId())
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .text("내용...")
                    .title("제목...")
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                    .attachId(attach.getAttachId())
                    .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                    .tagId(tag.getTagId())
                    .build()))
                    .build());
        }).isInstanceOf(AccessDeniedException.class);
    }


    @Test
    void 게시글_일반사용자_공지등록_실패() {
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                    .accountId(user.getAccountId())
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .text("내용...")
                    .title("제목...")
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
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            PostReadDto.Response res = postCreateUseCase.create(PostCreateDto.Request.builder()
                    .anonymousNickname("익명1")
                    .anonymousPassword("testest")
                    .boardId(board.getBoardId())
                    .isNotice(PostIsNotice.NOTICE)
                    .isSecret(PostIsSecret.SECRET)
                    .text("내용...")
                    .title("제목...")
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                    .attachId(attach.getAttachId())
                    .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                    .tagId(tag.getTagId())
                    .build()))
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(PostErrorCode.ONLY_ADMIN_SET_NOTICE.getMessage());
    }
}