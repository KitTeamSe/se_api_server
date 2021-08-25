package com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto.Response;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAccountMappingReadServiceTest {

  @Mock
  private AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
  @Mock
  private AccountJpaRepository accountJpaRepository;

  @InjectMocks
  private AuthorityGroupAccountMappingReadService authorityGroupAccountMappingReadService;

  @Test
  public void 권한그룹_계정_Mapping_읽기_성공() throws Exception{
    //given
    long id = 0L;
    long accountId = 0L;
    String accountIdString = "4-20글자";
    long groupId = 0L;
    String groupName = "2-30글자";

    Account account = Mockito.mock(Account.class);
    when(account.getAccountId()).thenReturn(accountId);
    when(account.getIdString()).thenReturn(accountIdString);
    AuthorityGroup authorityGroup = Mockito.mock(AuthorityGroup.class);
    when(authorityGroup.getAuthorityGroupId()).thenReturn(groupId);
    when(authorityGroup.getName()).thenReturn(groupName);

    AuthorityGroupAccountMapping authorityGroupAccountMapping = Mockito.mock(AuthorityGroupAccountMapping.class);
    when(authorityGroupAccountMapping.getAuthorityGroupAccountMappingId()).thenReturn(id);
    when(authorityGroupAccountMapping.getAccount()).thenReturn(account);
    when(authorityGroupAccountMapping.getAuthorityGroup()).thenReturn(authorityGroup);

    when(authorityGroupAccountMappingJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroupAccountMapping));
    //when
    AuthorityGroupAccountMappingReadDto.Response response = authorityGroupAccountMappingReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(id, response.getId()),
        () -> assertEquals(accountId, response.getAccountId()),
        () -> assertEquals(accountIdString, response.getAccountIdString()),
        () -> assertEquals(groupId, response.getGroupId()),
        () -> assertEquals(groupName, response.getGroupName())
    );
  }

  @Test
  public void 권한그룹_계정_Mapping_읽기_실패_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingReadService.read(0L));
    //then
    assertEquals(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_계정_Mapping_읽기_ALL_성공() throws Exception{
    //given
    long id = 0L;
    long accountId = 0L;
    String accountIdString = "4-20글자";
    long groupId = 0L;
    String groupName = "2-30글자";

    Account account = Mockito.mock(Account.class);
    when(account.getAccountId()).thenReturn(accountId);
    when(account.getIdString()).thenReturn(accountIdString);
    AuthorityGroup authorityGroup = Mockito.mock(AuthorityGroup.class);
    when(authorityGroup.getAuthorityGroupId()).thenReturn(groupId);
    when(authorityGroup.getName()).thenReturn(groupName);

    AuthorityGroupAccountMapping authorityGroupAccountMapping = Mockito.mock(AuthorityGroupAccountMapping.class);
    when(authorityGroupAccountMapping.getAuthorityGroupAccountMappingId()).thenReturn(id);
    when(authorityGroupAccountMapping.getAccount()).thenReturn(account);
    when(authorityGroupAccountMapping.getAuthorityGroup()).thenReturn(authorityGroup);

    PageRequest pageRequest = PageRequest.of(1, 10);
    PageImpl<AuthorityGroupAccountMapping> page = new PageImpl<>(Collections.singletonList(authorityGroupAccountMapping), pageRequest, 1);
    when(authorityGroupAccountMappingJpaRepository.findAll(pageRequest)).thenReturn(page);

    //when
    PageImpl<Response> response = authorityGroupAccountMappingReadService.readAll(pageRequest);
    //then
    assertAll(
        () -> assertEquals(id, response.getContent().get(0).getId()),
        () -> assertEquals(accountId, response.getContent().get(0).getAccountId()),
        () -> assertEquals(accountIdString, response.getContent().get(0).getAccountIdString()),
        () -> assertEquals(groupId, response.getContent().get(0).getGroupId()),
        () -> assertEquals(groupName, response.getContent().get(0).getGroupName()),
        () -> assertEquals(pageRequest, response.getPageable())
    );
  }

  @Test
  public void 권한그룹_계정_Mapping_읽기_BY_ACCOUNT_성공() throws Exception{
    //given
    long accountId = 0L;
    Account account = Account.builder().accountId(accountId).build();
    AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account, Mockito.mock(AuthorityGroup.class));
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.of(account));
    when(authorityGroupAccountMappingJpaRepository.findByAccountId(anyLong())).thenReturn(Collections.singletonList(authorityGroupAccountMapping));
    //when
    List<AuthorityGroupAccountMapping> responses = authorityGroupAccountMappingReadService.getMappedAuthorityGroupsByAccountId(accountId);
    //then
    assertEquals(authorityGroupAccountMapping, responses.get(0));
  }

  @Test
  public void 권한그룹_계정_Mapping_읽기_BY_ACCOUNT_실패_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAccountMappingReadService.getMappedAuthorityGroupsByAccountId(0L));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_계정_Mapping_확인() throws Exception{
    //given
    long accountId = 0L;
    Account account = Account.builder().accountId(accountId).build();
    AuthorityGroup authorityGroupTrue = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    AuthorityGroup authorityGroupFalse = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account, authorityGroupTrue);
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.of(account));
    when(authorityGroupAccountMappingJpaRepository.findByAccountId(anyLong())).thenReturn(Collections.singletonList(authorityGroupAccountMapping));
    //when
    boolean first = authorityGroupAccountMappingReadService.isAccountMappedToGroup(accountId, authorityGroupTrue);
    boolean second = authorityGroupAccountMappingReadService.isAccountMappedToGroup(accountId, authorityGroupFalse);
    //then
    assertAll(
        () -> assertEquals(true, first),
        () -> assertEquals(false, second)
    );
  }
}