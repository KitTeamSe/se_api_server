package com.se.apiserver.v1.multipartfile.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.presentation.response.Response;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileUploadErrorCode;
import java.util.Collections;
import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class MultipartFileUploadServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private MultipartFileUploadService multipartFileUploadService;

  private String UPLOAD_URL = "http://localhost:3000/for/test";
  private long MAX_FILE_SIZE = 100L;
  private String SERVICE_NAME = "test";

  @BeforeEach
  public void setUp(){
    ReflectionTestUtils.setField(multipartFileUploadService, "UPLOAD_URL", UPLOAD_URL);
    ReflectionTestUtils.setField(multipartFileUploadService, "MAX_FILE_SIZE", MAX_FILE_SIZE);
    ReflectionTestUtils.setField(multipartFileUploadService, "SERVICE_NAME", SERVICE_NAME);
  }

  private String createByteString(Long size) {
    return "test".repeat(size.intValue());
  }

  private MockMultipartFile createMockMultipartFile(String fileName, Long size) {
    byte[] content = createByteString(size).getBytes();
    return new MockMultipartFile(fileName, fileName+".txt", "text/plain", content);
  }

  @Test
  public void 파일_업로드_성공() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", 1L);
    List<MultipartFileUploadDto> datas = Collections.singletonList(new MultipartFileUploadDto("https://localhost:3000/savedName", "test"));
    Response<List<MultipartFileUploadDto>> response = new Response<>(HttpStatus.OK, "success", datas);
    ResponseEntity<Response<List<MultipartFileUploadDto>>> responseEntity = new ResponseEntity<>(response, response.getStatus());

    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<List<MultipartFileUploadDto>>>>any()
    )).thenReturn(responseEntity);

    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET", "/test");
    ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockHttpServletRequest);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);

    //when
    List<MultipartFileUploadDto> result = multipartFileUploadService.upload(file);
    //then
    assertEquals(datas.get(0).getDownloadUrl(), result.get(0).getDownloadUrl());
  }
  
  @Test
  public void 파일_업로드_실패_사이즈_초과() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", MAX_FILE_SIZE + 1);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileUploadService.upload(file));
    //then
    assertEquals(MultipartFileUploadErrorCode.FILE_SIZE_LIMIT_EXCEEDED, exception.getErrorCode());
  }
  
  @Test
  public void 파일_업로드_실패_사이즈_미만() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", 0L);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileUploadService.upload(file));
    //then
    assertEquals(MultipartFileUploadErrorCode.INVALID_FILE_SIZE, exception.getErrorCode());
  }
  
  @Test
  public void 파일_업로드_실패_HTTP_STATUS() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", 1L);

    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<List<MultipartFileUploadDto>>>>any()
    )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileUploadService.upload(file));
    //then
    assertEquals(MultipartFileDeleteErrorCode.FAILED_TO_PARSE_RESPONSE, exception.getErrorCode());
  }
  
  @Test
  public void 파일_업로드_실패_RESOURCE() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", 1L);

    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<List<MultipartFileUploadDto>>>>any()
    )).thenThrow(new ResourceAccessException("test"));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileUploadService.upload(file));
    //then
    assertEquals(MultipartFileUploadErrorCode.FAILED_TO_CONNECT_FILE_SERVER, exception.getErrorCode());
  }

  @Test
  public void 파일_업로드_실패_기타() throws Exception{
    //given
    MockMultipartFile file = createMockMultipartFile("test", 1L);

    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        Matchers.<HttpEntity<MultiValueMap<String, Object>>>any(),
        Matchers.<ParameterizedTypeReference<Response<List<MultipartFileUploadDto>>>>any()
    )).thenThrow(new NullPointerException("test"));

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> multipartFileUploadService.upload(file));
    //then
    assertEquals(MultipartFileUploadErrorCode.UNKNOWN_FILE_UPLOAD_ERROR, exception.getErrorCode());
  }
}