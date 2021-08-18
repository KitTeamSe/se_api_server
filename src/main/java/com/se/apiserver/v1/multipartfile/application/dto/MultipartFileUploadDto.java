package com.se.apiserver.v1.multipartfile.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipartFileUploadDto {

  private String downloadUrl;

  private String originalName;

  public MultipartFileUploadDto(String downloadUrl, String originalName) {
    this.downloadUrl = downloadUrl;
    this.originalName = originalName;
  }

  public void changeToInternalUrl(String internalUrl){
    this.downloadUrl = internalUrl;
  }
}
