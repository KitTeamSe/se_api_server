package com.se.apiserver.v1.tag.domain.usecase.taglistening;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.domain.entity.TagListening;
import com.se.apiserver.v1.tag.domain.error.TagListeningErrorCode;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningCreateDto;
import com.se.apiserver.v1.tag.infra.dto.taglistening.TagListeningReadDto;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.tag.infra.repository.TagListeningJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagListeningCreateUseCaseTest {
    @Autowired
    TagListeningJpaRepository tagListeningJpaRepository;
    @Autowired
    TagJpaRepository tagJpaRepository;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    TagListeningCreateUseCase tagListeningCreateUseCase;

    Account account1;
    Account account2;
    Tag tag1;
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


        tag1 = new Tag("새로운 태그");
        tagJpaRepository.save(tag1);
    }



    @Test
    void 수신태그_등록_본인_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account1.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        Long id = tagListeningCreateUseCase.create(TagListeningCreateDto.Request.builder()
                .tagId(tag1.getTagId())
                .build());
        TagListening response = tagListeningJpaRepository.getOne(id);
        //then
        Assertions.assertThat(response.getAccount().getAccountId()).isEqualTo(account1.getAccountId());
        Assertions.assertThat(response.getAccount().getIdString()).isEqualTo(account1.getIdString());
        Assertions.assertThat(response.getTag().getTagId()).isEqualTo(tag1.getTagId());
        Assertions.assertThat(response.getTag().getText()).isEqualTo(tag1.getText());
    }

    @Test
    void 수신태그_중복_실패() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account1.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        TagListening tagListening = new TagListening(account1, tag1);
        tagListeningJpaRepository.save(tagListening);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            tagListeningCreateUseCase.create(TagListeningCreateDto.Request.builder()
                    .tagId(tag1.getTagId())
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(TagListeningErrorCode.DUPLICATED.getMessage());
    }

}