package com.se.apiserver.v1.authority.application.service.authoritygroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupCreateDto.Request;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupCreateServiceTest {

  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;

  @InjectMocks
  private AuthorityGroupCreateService authorityGroupCreateService;

  @Test
  public void 권한그룹_생성_성공() throws Exception{
    //given
    AuthorityGroupCreateDto.Request request = Request.builder().name("2-30글자").description("2-100글자").build();
    when(authorityGroupJpaRepository.findByName(anyString())).thenReturn(Optional.ofNullable(null));
    AuthorityGroup authorityGroup = Mockito.mock(AuthorityGroup.class);
    when(authorityGroup.getAuthorityGroupId()).thenReturn(0L);
    when(authorityGroupJpaRepository.save(any(AuthorityGroup.class))).thenReturn(authorityGroup);
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupCreateService.create(request));
  }
  @Test
  public void 권한그룹_생성_실패_이름_중복() throws Exception{
    //given
    AuthorityGroupCreateDto.Request request = Request.builder().name("2-30글자").description("2-100글자").build();
    when(authorityGroupJpaRepository.findByName(anyString())).thenReturn(Optional.ofNullable(new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.DEFAULT)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupCreateService.create(request));
    //then
    assertEquals(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME, exception.getErrorCode());
  }
}