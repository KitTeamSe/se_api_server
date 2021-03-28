package com.se.apiserver.v1.tag.domain.usecase.taglistening;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.entity.TagListening;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.tag.infra.repository.TagListeningJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@Transactional
class TagListeningReadUseCaseTest {
    @Autowired
    TagListeningJpaRepository tagListeningJpaRepository;
    @Autowired
    TagJpaRepository tagJpaRepository;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    TagListeningReadUseCase tagListeningReadUseCase;

    Account account1;
    Account account2;
    Tag tag1;
    Tag tag2;

    Question question;

    void initData(){
        question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);
        account1 = Account.builder()
                .idString("test")
                .email("test@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("testpassword")
                .phoneNumber("55555555555")
                .studentId("20003156")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("ans")
                .build();
        accountJpaRepository.save(account1);

        account2 = Account.builder()
                .idString("test2")
                .email("test2@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test2")
                .nickname("test2")
                .password("tes2password")
                .phoneNumber("55525555555")
                .studentId("20003256")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("ans")
                .build();
        accountJpaRepository.save(account2);


        tag1 = Tag.builder()
                .text("새로운태그")
                .build();
        tagJpaRepository.save(tag1);

        tag2 = Tag.builder()
                .text("새로운태그2")
                .build();
        tagJpaRepository.save(tag2);
    }

    @Test
    void 수신태그_조회_본인_성공() {
        //given
        initData();
        TagListening tagListening = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account1.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        TagListeningReadDto.Response read = tagListeningReadUseCase.read(tagListening.getTagListeningId());
        //then
        Assertions.assertThat(read.getTagName()).isEqualTo(tag1.getText());
        Assertions.assertThat(read.getTagId()).isEqualTo(tag1.getTagId());
        Assertions.assertThat(read.getAccountIdString()).isEqualTo(account1.getIdString());
        Assertions.assertThat(read.getAccountId()).isEqualTo(account1.getAccountId());
    }

    @Test
    void 수신태그_조회_관리자_성공() {
        //given
        initData();
        TagListening tagListening = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account2.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("TAG_MANAGE"))));
        //when
        TagListeningReadDto.Response read = tagListeningReadUseCase.read(tagListening.getTagListeningId());
        //then
        Assertions.assertThat(read.getTagName()).isEqualTo(tag1.getText());
        Assertions.assertThat(read.getTagId()).isEqualTo(tag1.getTagId());
        Assertions.assertThat(read.getAccountIdString()).isEqualTo(account1.getIdString());
        Assertions.assertThat(read.getAccountId()).isEqualTo(account1.getAccountId());
    }

    @Test
    void 수신태그_조회_본인아님_실패() {
        //given
        initData();
        TagListening tagListening = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account2.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            TagListeningReadDto.Response read = tagListeningReadUseCase.read(tagListening.getTagListeningId());
        }).isInstanceOf(AccessDeniedException.class);
    }
    
    //

    @Test
    void 사용자의_수신_태그_목록_조회_본인_성공() {
        //given
        initData();
        TagListening tagListening1 = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening1);
        TagListening tagListening2 = TagListening.builder()
                .tag(tag2)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening2);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account1.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        List<TagListeningReadDto.Response> responseList = tagListeningReadUseCase.readMy(account1.getAccountId());
        //then
        Assertions.assertThat(responseList.size()).isEqualTo(2);
    }

    @Test
    void 사용자의_수신_태그_목록_조회_관리자_성공() {
        //given
        initData();
        TagListening tagListening1 = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening1);
        TagListening tagListening2 = TagListening.builder()
                .tag(tag2)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening2);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account2.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("TAG_MANAGE"))));
        //when
        List<TagListeningReadDto.Response> responseList = tagListeningReadUseCase.readMy(account1.getAccountId());
        //then
        Assertions.assertThat(responseList.size()).isEqualTo(2);
    }

    @Test
    void 사용자의_수신_태그_목록_조회_본인아님_실패() {
        //given
        initData();
        TagListening tagListening1 = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening1);
        TagListening tagListening2 = TagListening.builder()
                .tag(tag2)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening2);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account2.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            List<TagListeningReadDto.Response> responseList = tagListeningReadUseCase.readMy(account1.getAccountId());
        }).isInstanceOf(AccessDeniedException.class);
    }
    
    // 

    @Test
    void 수신태그_전체목록_관리자_성공() {
        //given
        initData();
        TagListening tagListening1 = TagListening.builder()
                .tag(tag1)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening1);
        TagListening tagListening2 = TagListening.builder()
                .tag(tag2)
                .account(account1)
                .build();
        tagListeningJpaRepository.save(tagListening2);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account2.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("TAG_MANAGE"))));
        //when
        PageImpl page = tagListeningReadUseCase.readAllByPaging(PageRequest.builder()
                .page(1)
                .size(10)
                .direction(Sort.Direction.ASC)
                .build().of());
        //then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
    }

}