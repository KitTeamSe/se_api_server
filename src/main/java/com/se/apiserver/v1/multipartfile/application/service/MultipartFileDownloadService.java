package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MultipartFileDownloadService extends MultipartFileService{

  private final RestTemplate restTemplate;

  @Value("${se-file-server.download-url}")
  private String DOWNLOAD_URL;

  public MultipartFileDownloadService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<Resource> download(String saveName){
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(DOWNLOAD_URL + "/" +saveName);

    try{
      return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Resource.class);
    }
    catch(HttpStatusCodeException e){
      throw super.getBusinessExceptionFromFileServerException(e);
    }
    catch(ResourceAccessException rae){
      throw new BusinessException(MultipartFileDownloadErrorCode.FAILED_TO_CONNECT_FILE_SERVER);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileDownloadErrorCode.UNKNOWN_FILE_DOWNLOAD_ERROR);
    }
  }
}