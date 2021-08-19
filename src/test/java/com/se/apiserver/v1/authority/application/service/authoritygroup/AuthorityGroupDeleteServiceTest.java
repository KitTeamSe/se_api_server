package com.se.apiserver.v1.authority.application.service.authoritygroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupDeleteServiceTest {
  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;

  @InjectMocks
  private AuthorityGroupDeleteService authorityGroupDeleteService;

  @Test
  public void 권한그룹_삭제_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroup));
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupDeleteService.delete(0L));
  }

  @Test
  public void 권한그룹_삭제_실패_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupDeleteService
        .delete(0L));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_삭제_실패_REMOVE_ANONYMOUS() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.ANONYMOUS);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroup));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupDeleteService
        .delete(0L));
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_ANONYMOUS_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_삭제_실패_REMOVE_DEFAULT() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.DEFAULT);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroup));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupDeleteService
        .delete(0L));
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_삭제_실패_REMOVE_SYSTEM() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.SYSTEM);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroup));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupDeleteService
        .delete(0L));
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_SYSTEM_GROUP, exception.getErrorCode());
  }
}