package com.se.apiserver.v1.authority.application.service.authoritygroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadService;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
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
class AuthorityGroupReadServiceTest {

  @Mock
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;
  @Mock
  private AuthorityGroupAuthorityMappingReadService authorityGroupAuthorityMappingReadService;

  @InjectMocks
  private AuthorityGroupReadService authorityGroupReadService;

  @Test
  public void 권한그룹_읽기_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    when(authorityGroupJpaRepository.findById(anyLong())).thenReturn(
        Optional.of(authorityGroup));
    when(authorityGroupAuthorityMappingReadService.readAllAuthorityByAuthorityGroup(any(AuthorityGroup.class))).thenReturn(
        Collections.emptyList());
    //when
    AuthorityGroupReadDto.Response response = authorityGroupReadService.read(0L);
    //then
    assertAll(
        () -> assertEquals(authorityGroup.getName(), response.getName()),
        () -> assertEquals(authorityGroup.getDescription(), response.getDescription()),
        () -> assertEquals(authorityGroup.getType(), response.getType())
    );
  }

  @Test
  public void 권한그룹_읽기_실패_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupReadService.read(0L));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_읽기_ALL_성공() throws Exception{
    //given
    PageRequest pageRequest = PageRequest.of(1, 10);
    long totalElement = 0L;
    PageImpl<AuthorityGroup> page = new PageImpl(Collections.emptyList(), pageRequest, totalElement);
    when(authorityGroupJpaRepository.findAll(pageRequest)).thenReturn(page);
    //when
    PageImpl<AuthorityGroupReadDto.Response> result = authorityGroupReadService.readAll(pageRequest);
    //then
    assertAll(
        () -> assertEquals(pageRequest, result.getPageable()),
        () -> assertEquals(totalElement, result.getTotalElements())
    );
  }

  @Test
  public void 권한그룹_읽기_DEFAULT_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.DEFAULT);
    when(authorityGroupJpaRepository.findByType(any(AuthorityGroupType.class))).thenReturn(
        Collections.singletonList(authorityGroup));
    //when
    AuthorityGroup result = authorityGroupReadService.getDefaultAuthorityGroup();
    //then
    assertEquals(authorityGroup, result);
  }

  @Test
  public void 권한그룹_읽기_TYPE_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("2-30글자", "2-100글자", AuthorityGroupType.NORMAL);
    when(authorityGroupJpaRepository.findByType(any(AuthorityGroupType.class))).thenReturn(
        Collections.singletonList(authorityGroup));
    //when
    List<AuthorityGroup> result = authorityGroupReadService.getAuthorityGroupsByType(AuthorityGroupType.NORMAL);
    //then
    assertEquals(authorityGroup, result.get(0));
  }

  @Test
  public void 권한그룹_읽기_TYPE_실패_결과_없음() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroupReadService.getAuthorityGroupsByType(AuthorityGroupType.NORMAL));
    //then
    assertEquals(AuthorityGroupErrorCode.NO_SUCH_AUTHORITY_GROUP, exception.getErrorCode());
  }
}