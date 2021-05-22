package com.se.apiserver.v1.multipartfile.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MultipartFileUploadDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class Response{

    private Long fileId;

    private String fileDownloadUrl;

    private String originalName;

    private String saveName;

    private String fileType;

    private Long size;
  }
}
