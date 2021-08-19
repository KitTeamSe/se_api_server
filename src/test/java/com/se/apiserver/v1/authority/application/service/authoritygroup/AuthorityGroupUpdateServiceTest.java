package com.se.apiserver.v1.authority.application.service.authoritygroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupUpdateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupUpdateDto.Request;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
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
class AuthorityGroupUpdateServiceTest {

  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;

  @InjectMocks
  private AuthorityGroupUpdateService authorityGroupUpdateService;

  @Test
  public void 권한그룹_수정_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = Mockito.mock(AuthorityGroup.class);
    long id = 0L;
    when(authorityGroup.getAuthorityGroupId()).thenReturn(id);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroup));
    AuthorityGroupUpdateDto.Request request = new Request(0L, "2-30글자", "2-100글자");
    when(authorityGroupJpaRepository.save(any(AuthorityGroup.class))).thenReturn(authorityGroup);
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupUpdateService.update(request));
  }

  @Test
  public void 권한그룹_수정_실패_이름_중복() throws Exception{
    //given
    when(authorityGroupJpaRepository.findByName(anyString())).thenReturn(Optional.of(Mockito.mock(AuthorityGroup.class)));
    AuthorityGroupUpdateDto.Request request = new Request(0L, "2-30글자", "2-100글자");
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupUpdateService.update(request));
    //then
    assertEquals(AuthorityGroupErrorCode.DUPLICATED_GROUP_NAME, exception.getErrorCode());
  }
  
  @Test
  public void 권한그룹_수정_실패_ID_불일치() throws Exception{
    //given
    AuthorityGroupUpdateDto.Request request = new Request(0L, "2-30글자", "2-100글자");
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupUpdateService.update(request));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }
}