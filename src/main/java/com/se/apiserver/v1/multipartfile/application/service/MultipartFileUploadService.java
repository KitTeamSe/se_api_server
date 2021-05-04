package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileUploadErrorCode;
import com.se.apiserver.v1.multipartfile.infra.config.MultipartFileProperties;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MultipartFileUploadService {
  private final String FILE_SERVER_BASE_URL;
  private final long MAX_FILE_SIZE;

  @Autowired
  public MultipartFileUploadService(MultipartFileProperties properties){
    FILE_SERVER_BASE_URL = "http://" + properties.getFileServerDomain() + "/file/upload/uploadFile/";
    MAX_FILE_SIZE = properties.getMaxFileSize();
  }

  public String storeFile(MultipartFile file) {
    if(file.getSize() <= 0)
      throw new BusinessException(MultipartFileUploadErrorCode.INVALID_FILE_SIZE);

    if(file.getSize() >= MAX_FILE_SIZE)
      throw new BusinessException(MultipartFileUploadErrorCode.FILE_SIZE_LIMIT_EXCEEDED);

    // Body 생성
    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", file.getResource());

    // Body + Header
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, new HttpHeaders());

    // Post 요청
    try{
      RestTemplate restTemplate = new RestTemplate();

      String response = restTemplate.postForObject(
          new URI(FILE_SERVER_BASE_URL),
          request,
          String.class);

      // 다운로드 URL 생성
      String hostBaseUrl = getCurrentHostUrl() + "/multipart-file/download/";
      String fileName = response.substring(response.lastIndexOf('/') + 1);
      return hostBaseUrl + fileName;
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileUploadErrorCode.INTERNAL_FILE_SERVER_ERROR);
    }
  }

  private String getCurrentHostUrl(){
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
  }
}
