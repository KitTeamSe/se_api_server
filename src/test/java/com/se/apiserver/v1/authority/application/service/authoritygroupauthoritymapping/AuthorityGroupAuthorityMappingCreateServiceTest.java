package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto.Request;
import com.se.apiserver.v1.authority.application.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAuthorityMappingCreateServiceTest {

  @Mock
  private AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;
  @Mock
  private AuthorityJpaRepository authorityJpaRepository;
  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;

  @InjectMocks
  private AuthorityGroupAuthorityMappingCreateService authorityGroupAuthorityMappingCreateService;

  @Test
  public void 권한그룹_권한_Mapping_생성_성공() throws Exception{
    //given
    long groupId = 0L;
    long authorityId = 0L;
    AuthorityGroupAuthorityMappingCreateDto.Request request = new Request(groupId, authorityId);
    when(authorityJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Authority.class)));
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(AuthorityGroup.class)));
    //when
    System.out.println(authorityGroupAuthorityMappingCreateService.create(request));
    //then
    assertDoesNotThrow(() -> authorityGroupAuthorityMappingCreateService.create(request));
  }

  @Test
  public void 권한그룹_권한_Mapping_생성_실패_ALREADY_EXIST() throws Exception{
    //given
    long groupId = 0L;
    long authorityId = 0L;
    AuthorityGroupAuthorityMappingCreateDto.Request request = new Request(groupId, authorityId);
    when(authorityGroupAuthorityMappingJpaRepository.findByAuthorityAndAuthorityGroupId(anyLong(), anyLong())).thenReturn(
        Optional.of(Mockito.mock(AuthorityGroupAuthorityMapping.class)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAuthorityMappingCreateService.create(request));
    //then
    assertEquals(AuthorityGroupAuthorityMappingErrorCode.ALREADY_EXIST, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_권한_Mapping_생성_실패_authority_ID_불일치() throws Exception{
    //given
    long groupId = 0L;
    long authorityId = 0L;
    AuthorityGroupAuthorityMappingCreateDto.Request request = new Request(groupId, authorityId);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAuthorityMappingCreateService.create(request));
    //then
    assertEquals(AuthorityErrorCode.NO_SUCH_AUTHORITY, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_권한_Mapping_생성_실패_authority_group_ID_불일치() throws Exception{
    //given
    long groupId = 0L;
    long authorityId = 0L;
    AuthorityGroupAuthorityMappingCreateDto.Request request = new Request(groupId, authorityId);
    when(authorityJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Authority.class)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAuthorityMappingCreateService.create(request));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }
}