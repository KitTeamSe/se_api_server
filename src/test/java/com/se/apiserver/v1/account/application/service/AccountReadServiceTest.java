package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountReadDto;
import com.se.apiserver.v1.account.application.dto.AccountReadDto.AccountSearchRequest;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountQueryRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class AccountReadServiceTest {
  
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private AccountQueryRepository accountQueryRepository;

  @InjectMocks
  private AccountReadService accountReadService;
  
  private static String id;
  private static Pageable pageable;
  private static AccountSearchRequest accountSearchRequest;
  
  @BeforeAll
  static void setup(){
    id = "4글자-20글자";
    PageRequest pageRequest = PageRequest.builder().page(0).size(10).direction(Direction.ASC).build();
    pageable = pageRequest.of();
    accountSearchRequest = AccountSearchRequest.builder()
        .name("검색할 이름")
        .nickname("검색할 닉네임")
        .email("검색할 이메일")
        .studentId("검색할 학번")
        .phoneNumber("검색할 휴대폰 번호")
        .type(AccountType.STUDENT)
        .pageRequest(pageRequest)
        .build();
  }
  
  @Test
  public void 회원찾기_ID_성공_회원() throws Exception{
    //given
    String studentId = "8글자-20글자";
    when(accountJpaRepository.findByIdString(id)).thenReturn(
        Optional.ofNullable(Account.builder().idString(id).studentId(studentId).build()));
    when(accountContextService.isOwner(any(Account.class))).thenReturn(true);
    //when
    AccountReadDto.Response response = accountReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(id, response.getIdString()),
        () -> assertEquals(studentId, response.getStudentId())
    );
  }

  @Test
  public void 회원찾기_ID_성공_관리자() throws Exception{
    //given
    String studentId = "8글자-20글자";
    when(accountJpaRepository.findByIdString(id)).thenReturn(
        Optional.ofNullable(Account.builder().idString(id).studentId(studentId).build()));
    when(accountContextService.hasAuthority(Account.MANAGE_TOKEN)).thenReturn(true);
    //when
    AccountReadDto.Response response = accountReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(id, response.getIdString()),
        () -> assertEquals(studentId, response.getStudentId())
    );
  }

  @Test
  public void 회원찾기_ID_성공_권한_없음() throws Exception{
    //given
    String studentId = "8글자-20글자";
    when(accountJpaRepository.findByIdString(id)).thenReturn(
        Optional.ofNullable(Account.builder().idString(id).studentId(studentId).build()));
    //when
    AccountReadDto.Response response = accountReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(id, response.getIdString()),
        () -> assertEquals(null, response.getStudentId())
    );
  }

  @Test
  public void 회원찾기_ID_실패_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountReadService.read(id));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 회원찾기_MINE_성공() throws Exception{
    //given
    when(accountContextService.getContextAccount()).thenReturn(Account.builder().idString(id).build());
    //when
    AccountReadDto.Response response = accountReadService.readMyAccount();
    //then
    assertEquals(id, response.getIdString());
  }

  @Test
  public void 회원찾기_ALL_성공() throws Exception{
    //given
    Account sample = Account.builder().idString(id).build();
    long totalElement = 1L;
    when(accountJpaRepository.findAll(pageable)).thenReturn(
        new PageImpl(Collections.singletonList(sample), pageable, totalElement));
    //when
    PageImpl<AccountReadDto.Response> page = accountReadService.readAll(pageable);
    AccountReadDto.Response response = page.getContent().get(0);
    //then
    assertAll(
        () -> assertEquals(id, response.getIdString()),
        () -> assertEquals(pageable, page.getPageable()),
        () -> assertEquals(totalElement, page.getTotalElements())
    );
  }

  @Test
  public void 회원찾기_성공_검색() throws Exception{
    //given
    Account sample = Account.builder().idString(id).build();
    long totalElement = 1L;
    when(accountQueryRepository.search(accountSearchRequest)).thenReturn(
        new PageImpl(Collections.singletonList(sample), pageable, totalElement)
    );
    //when
    PageImpl<AccountReadDto.Response> page = accountReadService.search(accountSearchRequest);
    AccountReadDto.Response response = page.getContent().get(0);
    //then
    assertAll(
        () -> assertEquals(id, response.getIdString()),
        () -> assertEquals(pageable, page.getPageable()),
        () -> assertEquals(totalElement, page.getTotalElements())
    );
  }
}