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

@Service
@RequiredArgsConstructor
public class MultipartFileUploadService {

  private final String domain;

  @Autowired
  public MultipartFileUploadService(MultipartFileProperties properties){
    domain = properties.getDomain();
  }

  public String storeFile(MultipartFile file) {
    // Body 생성
    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", file.getResource());

    // Header 생성
    HttpHeaders headers = new HttpHeaders();

    // Body + Header
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);

    // Post 요청
    RestTemplate restTemplate = new RestTemplate();

    try{
      return restTemplate.postForObject(
          new URI("http://" + domain + "/file/upload/uploadFile/1"),
          request,
          String.class);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileUploadErrorCode.UNKNOWN_UPLOAD_ERROR_CAUSED);
    }
  }
}
