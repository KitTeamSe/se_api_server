package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class MultipartFileDownloadService extends MultipartFileService{

  @Value("${se-file-server.download-url}")
  private String DOWNLOAD_URL;

  public ResponseEntity<Resource> download(String saveName){
    RestTemplate rest = new RestTemplate();
    String downloadUrl = DOWNLOAD_URL + saveName;

    try{
      return rest.exchange(new URI(downloadUrl), HttpMethod.GET, null, Resource.class);
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