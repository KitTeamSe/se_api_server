
package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto.Response;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileUploadErrorCode;
import java.net.URI;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultipartFileUploadService extends MultipartFileService {

  @Value("${se-file-server.upload-url}")
  private String UPLOAD_URL;

  public Response upload(MultipartFile file) {
    // 파일 크기 검증
    validateFileSize(file.getSize());

    // Body 생성
    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", file.getResource());

    // Body + Header
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, new HttpHeaders());

    // Post 요청
    try{
      RestTemplate restTemplate = new RestTemplate();

      Response response = restTemplate.postForObject(
          new URI(UPLOAD_URL),
          request,
          Response.class
      );

      // 외부 다운로드 URL 설정 후 반환
      response.setFileDownloadUrl(createDownloadUrl(response.getSaveName()));
      return response;
    }
    catch(HttpStatusCodeException e){
      throw super.getBusinessExceptionFromFileServerException(e);
    }
    catch(ResourceAccessException rae){
      throw new BusinessException(MultipartFileUploadErrorCode.FAILED_TO_CONNECT_FILE_SERVER);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileUploadErrorCode.UNKNOWN_FILE_UPLOAD_ERROR);
    }
  }

  public List<Response> upload(MultipartFile[] files) {
    // 파일 크기 검증
    for(MultipartFile file : files)
      validateFileSize(file.getSize());

    // Body 생성
    MultiValueMap<String, MultipartFile[]> parameters = new LinkedMultiValueMap<>();
    parameters.add("files", files);

    // Body + Header
    HttpEntity<MultiValueMap<String, MultipartFile[]>> request = new HttpEntity<>(parameters, new HttpHeaders());

    // Post 요청
    try{
      RestTemplate restTemplate = new RestTemplate();

      List<Response> responses = restTemplate.postForObject(
          new URI(UPLOAD_URL + "uploadMultipleFiles"),
          request,
          List.class
      );

      // 다운로드 URL 설정 후 반환
      for(Response r : responses){
        r.setFileDownloadUrl(createDownloadUrl(r.getSaveName()));
      }

      return responses;
    }
    catch(HttpStatusCodeException e){
      throw super.getBusinessExceptionFromFileServerException(e);
    }
    catch(ResourceAccessException rae){
      throw new BusinessException(MultipartFileDownloadErrorCode.FAILED_TO_CONNECT_FILE_SERVER);
    }
    catch (Exception e){
      throw new BusinessException(MultipartFileUploadErrorCode.UNKNOWN_FILE_UPLOAD_ERROR);
    }
  }

  private void validateFileSize(long fileSize){
    if(fileSize <= 0)
      throw new BusinessException(MultipartFileUploadErrorCode.INVALID_FILE_SIZE);

    if(fileSize >= MAX_FILE_SIZE)
      throw new BusinessException(MultipartFileUploadErrorCode.FILE_SIZE_LIMIT_EXCEEDED);
  }

  private String createDownloadUrl(String saveName){
    String hostBaseUrl = super.getCurrentHostUrl() + "/multipart-file/download/";
    return hostBaseUrl + saveName;
  }
}