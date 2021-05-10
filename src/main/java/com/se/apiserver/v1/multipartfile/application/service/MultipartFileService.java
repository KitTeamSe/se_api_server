package com.se.apiserver.v1.multipartfile.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.FileServerErrorCodeProxy;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;
import com.se.apiserver.v1.multipartfile.infra.config.MultipartFileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public abstract class MultipartFileService {

  protected final String BASE_URL;

  @Autowired
  public MultipartFileService(MultipartFileProperties properties){
    BASE_URL = "http://" + properties.getFileServerDomain() + "/file/";
  }

  protected BusinessException getBusinessExceptionFromFileServerResponse(HttpStatusCodeException e){
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
