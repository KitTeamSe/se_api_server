package com.se.apiserver.v1.multipartfile.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultiPartFileUploadDto {

  private String downloadUrl;

  public MultiPartFileUploadDto(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public void changeToInternalUrl(String internalUrl){
    this.downloadUrl = internalUrl;
  }
}
