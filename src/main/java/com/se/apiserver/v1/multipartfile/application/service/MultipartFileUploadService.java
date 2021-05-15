
package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileDownloadErrorCode;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileUploadErrorCode;
import java.net.URI;

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

  public String upload(MultipartFile file) {
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

      String internalFileDownloadUrl = restTemplate.postForObject(
              new URI(UPLOAD_URL),
              request,
              String.class);

      // 내부 파일 서버 URL로부터, 외부 다운로드 URL 생성 후 반환
      return createExternalDownloadUrl(internalFileDownloadUrl);
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

  private String createExternalDownloadUrl(String fileServerResponse){
    String hostBaseUrl = super.getCurrentHostUrl() + "/multipart-file/download/";
    String fileName = fileServerResponse.substring(fileServerResponse.lastIndexOf('/') + 1);
    return hostBaseUrl + fileName;
  }
}