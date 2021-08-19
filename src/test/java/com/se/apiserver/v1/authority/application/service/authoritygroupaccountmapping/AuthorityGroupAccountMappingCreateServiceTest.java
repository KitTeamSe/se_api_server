package com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto.Request;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAccountMappingCreateServiceTest {

  @Mock
  private AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;

  @InjectMocks
  private AuthorityGroupAccountMappingCreateService authorityGroupAccountMappingCreateService;

  private static AuthorityGroupAccountMappingCreateDto.Request request;

  @BeforeAll
  static void setUp(){
    request = new Request(0L, 0L);
  }

  @Test
  public void 권한그룹_계정_Mapping_생성_성공() throws Exception{
    //given
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Account.class)));
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(AuthorityGroup.class)));
    //when
    //then
    assertDoesNotThrow(() -> authorityGroupAccountMappingCreateService.create(request));
  }

  @Test
  public void 권한그룹_계정_Mapping_생성_실패_중복_Mapping() throws Exception{
    //given
    when(authorityGroupAccountMappingJpaRepository.findByAccountIdAndAuthorityGroupId(anyLong(), anyLong()))
        .thenReturn(Optional.of(Mockito.mock(AuthorityGroupAccountMapping.class)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingCreateService.create(request));
    //then
    assertEquals(AuthorityGroupAccountMappingErrorCode.ALREADY_EXIST, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_계정_Mapping_생성_실패_Account_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingCreateService.create(request));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_계정_Mapping_생성_실패_Authority_group_ID_불일치() throws Exception{
    //given
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Account.class)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingCreateService.create(request));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }
}