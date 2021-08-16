
package com.se.apiserver.v1.multipartfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.presentation.response.Response;
import com.se.apiserver.v1.multipartfile.application.dto.MultiPartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.error.MultipartFileUploadErrorCode;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MultipartFileUploadService extends MultipartFileService {

  private final RestTemplate restTemplate;

  @Value("${se-file-server.upload-url}")
  private String UPLOAD_URL;

  public MultipartFileUploadService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<MultiPartFileUploadDto> upload(MultipartFile... files) {
    // 파일 크기 검증
    for(MultipartFile file: files) {
      validateFileSize(file.getSize());
    }
    // header for multipart form
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    // Body
    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    for(MultipartFile file: files) {
      parameters.add("files", file.getResource());
    }

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UPLOAD_URL);
    builder.queryParam("service", SERVICE_NAME);

    // Body + Header
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
    // Post 요청
    try{
      List<MultiPartFileUploadDto> responseData = Objects.requireNonNull(restTemplate.exchange(
          builder.toUriString(),
          HttpMethod.POST,
          request,
          new ParameterizedTypeReference<Response<List<MultiPartFileUploadDto>>>() {
          }
      ).getBody()).getData();

      for(MultiPartFileUploadDto data: responseData)
        data.changeToInternalUrl(
            createInternalUrl(data.getDownloadUrl())
      );

      return responseData;
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

  private void validateFileSize(long fileSize){
    if(fileSize <= 0)
      throw new BusinessException(MultipartFileUploadErrorCode.INVALID_FILE_SIZE);

    if(fileSize >= MAX_FILE_SIZE)
      throw new BusinessException(MultipartFileUploadErrorCode.FILE_SIZE_LIMIT_EXCEEDED);
  }

  private String createInternalUrl(String downloadUrl){
    String saveName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
    String hostBaseUrl = super.getCurrentHostUrl() + "/multipart-file/download/";
    return hostBaseUrl + saveName;
  }
}