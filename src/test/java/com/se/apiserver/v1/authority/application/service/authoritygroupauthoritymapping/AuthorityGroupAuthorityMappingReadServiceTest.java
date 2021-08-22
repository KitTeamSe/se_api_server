package com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupAuthorityMappingErrorCode;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AuthorityGroupAuthorityMappingReadServiceTest {

  @Mock
  private AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;

  @InjectMocks
  private AuthorityGroupAuthorityMappingReadService authorityGroupAuthorityMappingReadService;

  @Test
  public void 권한그룹_권한_Mapping_읽기_성공() throws Exception{
    //given
    long id = 0L;
    Authority authority = new Authority("2-40글자", "2-40글자");
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = new AuthorityGroupAuthorityMapping(authority, authorityGroup);
    when(authorityGroupAuthorityMappingJpaRepository.findById(anyLong())).thenReturn(Optional.of(authorityGroupAuthorityMapping));
    //when
    AuthorityGroupAuthorityMappingReadDto.Response response = authorityGroupAuthorityMappingReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(authority.getNameKor(), response.getAuthorityIdNameKor()),
        () -> assertEquals(authority.getNameEng(), response.getAuthorityIdNameEng()),
        () -> assertEquals(authorityGroup.getName(), response.getGroupName())
    );
  }

  @Test
  public void 권한그룹_권한_Mapping_읽기_실패_ID_불일치() throws Exception{
    //given
    long id = 0L;
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupAuthorityMappingReadService.read(id));
    //then
    assertEquals(AuthorityGroupAuthorityMappingErrorCode.NO_SUCH_MAPPING, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_권한_Mapping_읽기_ALL() throws Exception{
    //given
    long id = 0L;
    long totalElement = 1L;
    PageRequest pageRequest = PageRequest.of(0, 10);
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = mock(AuthorityGroupAuthorityMapping.class);
    when(authorityGroupAuthorityMapping.getAuthorityGroup()).thenReturn(mock(AuthorityGroup.class));
    when(authorityGroupAuthorityMapping.getAuthority()).thenReturn(mock(Authority.class));
    when(authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId()).thenReturn(id);
    PageImpl<AuthorityGroupAuthorityMapping> page = new PageImpl<>(Collections.singletonList(authorityGroupAuthorityMapping), pageRequest, totalElement);
    when(authorityGroupAuthorityMappingJpaRepository.findAll(pageRequest)).thenReturn(page);
    //when
    PageImpl<AuthorityGroupAuthorityMappingReadDto.Response> response = authorityGroupAuthorityMappingReadService.readAll(pageRequest);
    //then
    assertAll(
        () -> assertEquals(id, response.getContent().get(0).getId()),
        () -> assertEquals(pageRequest, response.getPageable()),
        () -> assertEquals(totalElement, response.getTotalElements())
    );
  }

  @Test
  public void 권한그룹_권한_Mapping_읽기_ALL_BY_GROUP() throws Exception{
    //given
    Authority authority = new Authority("2-40글자", "2-40글자");
    AuthorityGroup authorityGroup = mock(AuthorityGroup.class);
    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = new AuthorityGroupAuthorityMapping(authority, authorityGroup);
    when(authorityGroupAuthorityMappingJpaRepository.findAllByAuthorityGroup(any(AuthorityGroup.class))).thenReturn(Collections.singletonList(authorityGroupAuthorityMapping));
    //when
    List<Authority> response = authorityGroupAuthorityMappingReadService.readAllAuthorityByAuthorityGroup(authorityGroup);
    //then
    assertEquals(authority, response.get(0));
  }
}