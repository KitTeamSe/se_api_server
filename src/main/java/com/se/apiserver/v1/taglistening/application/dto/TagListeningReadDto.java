package com.se.apiserver.v1.taglistening.application.dto;

import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TagListeningReadDto {

  @Getter
  @NoArgsConstructor
  @Builder
  @ApiModel("수신 태그 조회 응답")
  static public class Response {

    private Long tagListeningId;

    @Min(1)
    private Long tagId;
    private String tagName;
    @Min(1)
    private Long accountId;
    private String accountIdString;

    public Response(Long tagListeningId, Long tagId, String tagName, Long accountId,
        String accountIdString) {
      this.tagListeningId = tagListeningId;
      this.tagId = tagId;
      this.tagName = tagName;
      this.accountId = accountId;
      this.accountIdString = accountIdString;
    }

    static public Response fromEntity(TagListening tagListening) {
      return Response.builder()
          .accountId(tagListening.getAccount().getAccountId())
          .accountIdString(tagListening.getAccount().getIdString())
          .tagId(tagListening.getTag().getTagId())
          .tagName(tagListening.getTag().getText())
          .tagListeningId(tagListening.getTagListeningId())
          .build();
    }
  }
}
