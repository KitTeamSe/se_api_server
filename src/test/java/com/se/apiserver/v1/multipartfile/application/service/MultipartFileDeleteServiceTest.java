package com.se.apiserver.v1.multipartfile.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.presentation.response.Response;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class MultipartFileDeleteServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private MultipartFileDeleteService multipartFileDeleteService;

  private String DELETE_URL = "http://localhost:3000/for/test";
  private long MAX_FILE_SIZE = 100L;
  private String SERVICE_NAME = "test";

  @BeforeEach
  public void setUp(){
    ReflectionTestUtils.setField(multipartFileDeleteService, "DELETE_URL", DELETE_URL);
    ReflectionTestUtils.setField(multipartFileDeleteService, "MAX_FILE_SIZE", MAX_FILE_SIZE);
    ReflectionTestUtils.setField(multipartFileDeleteService, "SERVICE_NAME", SERVICE_NAME);
  }
  
  @Test
  public void 파일_삭제_성공() throws Exception{
    //given
    
    //when
    
    //then
    assertDoesNotThrow(() -> multipartFileDeleteService.delete("test"));
  }
  
  @Test
  public void 파일_삭제_실패_HTTP_STATUS() throws Exception{
    //given
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<String>>>any()
    )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileDeleteService.delete("test"));
    //then
    assertEquals(MultipartFileDeleteErrorCode.FAILED_TO_PARSE_RESPONSE, exception.getErrorCode());
  }
  
  @Test
  public void 파일_삭제_실패_RESOURCE() throws Exception{
    //given
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<String>>>any()
    )).thenThrow(new ResourceAccessException("test"));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileDeleteService.delete("test"));
    //then
    assertEquals(MultipartFileDeleteErrorCode.FAILED_TO_CONNECT_FILE_SERVER, exception.getErrorCode());
  }
  
  @Test
  public void 파일_삭제_실패_기타() throws Exception{
    //given
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<String>>>any()
    )).thenThrow(new NullPointerException("test"));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileDeleteService.delete("test"));
    //then
    assertEquals(MultipartFileDeleteErrorCode.UNKNOWN_FILE_DELETE_ERROR, exception.getErrorCode());
  }
}