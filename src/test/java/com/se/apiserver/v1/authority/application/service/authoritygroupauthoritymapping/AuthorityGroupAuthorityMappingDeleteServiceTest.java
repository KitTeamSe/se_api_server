package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAuthorityMappingDeleteServiceTest {

  @Mock
  private AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;

  @InjectMocks
  private AuthorityGroupAuthorityMappingDeleteService authorityGroupAuthorityMappingDeleteService;

  @Test
  public void 권한그룹_권한_삭제_성공() throws Exception{
    //given
    long id = 0L;
    when(authorityGroupAuthorityMappingJpaRepository.findById(id)).thenReturn(Optional.of(mock(AuthorityGroupAuthorityMapping.class)));
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupAuthorityMappingDeleteService.delete(id));
  }

  @Test
  public void 권한그룹_권한_삭제_실패_ID_불일치() throws Exception{
    //given
    long id = 0L;
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAuthorityMappingDeleteService.delete(id));
    //then
    assertEquals(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING, exception.getErrorCode())  ;
  }
}