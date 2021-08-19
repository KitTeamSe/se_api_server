package com.se.apiserver.v1.multipartfile.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.FileServerErrorCodeProxy;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
public abstract class MultipartFileService {

  @Value("${se-file-server.max-file-size}")
  protected long MAX_FILE_SIZE;

  @Value("${service-name}")
  protected String SERVICE_NAME;

  protected BusinessException getBusinessExceptionFromFileServerException(HttpStatusCodeException e){
    try{
      String errorCodeJson = e.getResponseBodyAsString();
      FileServerErrorCodeProxy errorCode = new ObjectMapper().readValue(errorCodeJson, FileServerErrorCodeProxy.class);
      return new BusinessException(errorCode);
    }
    catch (JsonProcessingException jpe){
      return new BusinessException(MultipartFileDeleteErrorCode.FAILED_TO_PARSE_RESPONSE);
    }
  }

  protected String getCurrentHostUrl(){
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
  }
}