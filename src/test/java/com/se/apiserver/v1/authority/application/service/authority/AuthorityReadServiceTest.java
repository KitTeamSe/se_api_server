package com.se.apiserver.v1.authority.application.service.authority;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.application.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AuthorityReadServiceTest {

  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private AuthorityReadService authorityReadService;

  @Test
  public void 권한_읽기_성공_ID() throws Exception{
    //given
    Long id = 0L;
    Authority authority = new Authority("2-20글자", "2-20글자");
    when(authorityJpaRepository.findById(id)).thenReturn(Optional.ofNullable(authority));
    //when
    AuthorityReadDto.Response response = authorityReadService.read(id);
    //then
    assertAll(
      () -> assertEquals(authority.getNameEng(), response.getNameEng()),
      () -> assertEquals(authority.getNameKor(), response.getNameKor())
    );
  }

  @Test
  public void 권한_읽기_실패_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityReadService.read(0L));
    //then
    assertEquals(AuthorityErrorCode.NO_SUCH_AUTHORITY, exception.getErrorCode());
  }

  @Test
  public void 권한_읽기_성공_ALL() throws Exception{
    //given
    PageRequest pageRequest = PageRequest.of(1, 10);
    Authority authority = new Authority("2-20글자","2-20글자");
    PageImpl<Authority> page = new PageImpl(Collections.singletonList(authority), pageRequest, 1);
    when(authorityJpaRepository.findAll(pageRequest)).thenReturn(page);
    //when
    PageImpl<AuthorityReadDto.Response> result = authorityReadService.readAll(pageRequest);
    //then
    assertAll(
      () -> assertEquals(pageRequest, result.getPageable()),
      () -> assertEquals(authority.getNameKor(), result.getContent().get(0).getNameKor()),
      () -> assertEquals(authority.getNameEng(), result.getContent().get(0).getNameEng())
    );
  }
}