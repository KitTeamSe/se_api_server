package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;
import com.se.apiserver.v1.multipartfile.infra.config.MultipartFileProperties;

import java.net.URI;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class MultipartFileDeleteService extends MultipartFileService {

  private final String DELETE_URL;

  public MultipartFileDeleteService(MultipartFileProperties properties) {
    super(properties);
    DELETE_URL = super.BASE_URL + "delete/";
  }

  public void delete(final String saveName){
    RestTemplate rest = new RestTemplate();

    String downloadUrl = DELETE_URL + saveName;

    try{
      rest.exchange(new URI(downloadUrl), HttpMethod.GET, null, Resource.class);
    }
    catch(HttpStatusCodeException e){
      throw super.getBusinessExceptionFromFileServerException(e);
    }
    catch(ResourceAccessException rae){
      throw new BusinessException(MultipartFileDownloadErrorCode.FAILED_TO_CONNECT_FILE_SERVER);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileDeleteErrorCode.UNKNOWN_FILE_DELETE_ERROR);
    }
  }

  public void deleteByDownloadUrl(final String downloadUrl){
    if(downloadUrl == null || downloadUrl.isEmpty())
      throw new BusinessException(MultipartFileDeleteErrorCode.EMPTY_DOWNLOAD_URL);
    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
    this.delete(fileName);
  }
}
