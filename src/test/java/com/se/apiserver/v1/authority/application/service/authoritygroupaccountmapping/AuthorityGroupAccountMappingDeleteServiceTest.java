package com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAccountMappingDeleteServiceTest {

  @Mock
  private AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;

  @InjectMocks
  private AuthorityGroupAccountMappingDeleteService authorityGroupAccountMappingDeleteService;

  @Test
  public void 권한그룹_계정_Mapping_삭제_성공() throws Exception{
    //given
    when(authorityGroupAccountMappingJpaRepository.findById(anyLong())).thenReturn(
        Optional.of(Mockito.mock(AuthorityGroupAccountMapping.class)));
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupAccountMappingDeleteService.delete(0L));
  }
  
  @Test
  public void 권한그룹_계정_Mapping_삭제_실패_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingDeleteService.delete(0L));
    //then
    assertEquals(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING, exception.getErrorCode());
  }
}