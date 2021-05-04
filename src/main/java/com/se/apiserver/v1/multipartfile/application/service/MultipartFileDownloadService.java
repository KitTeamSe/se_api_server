package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;
import com.se.apiserver.v1.multipartfile.infra.config.MultipartFileProperties;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MultipartFileDownloadService {

  private final String BASE_URL;

  @Autowired
  public MultipartFileDownloadService(MultipartFileProperties properties){
    BASE_URL = "http://" + properties.getFileServerDomain() + "/file/download/";
  }

  public ResponseEntity<Resource> download(String saveName){
    RestTemplate rest = new RestTemplate();
    String downloadUrl = BASE_URL + saveName;

    try{
      return rest.exchange(new URI(downloadUrl), HttpMethod.GET, null, Resource.class);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileDownloadErrorCode.INTERNAL_FILE_SERVER_ERROR);
    }
  }
}
