package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.presentation.response.Response;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDeleteErrorCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MultipartFileDeleteService extends MultipartFileService {

  private final RestTemplate restTemplate;

  @Value("${se-file-server.delete-url}")
  private String DELETE_URL;

  public MultipartFileDeleteService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void delete(final String saveName){
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(DELETE_URL + "/" + SERVICE_NAME + "/" + saveName);

    try{
      restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null,
          new ParameterizedTypeReference<Response<String>>() {
          }
      );
    }
    catch(HttpStatusCodeException e){
      throw super.getBusinessExceptionFromFileServerException(e);
    }
    catch(ResourceAccessException rae){
      throw new BusinessException(MultipartFileDeleteErrorCode.FAILED_TO_CONNECT_FILE_SERVER);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileDeleteErrorCode.UNKNOWN_FILE_DELETE_ERROR);
    }
  }
}