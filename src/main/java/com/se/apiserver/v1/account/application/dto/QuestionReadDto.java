package com.se.apiserver.v1.account.application.dto;

import com.se.apiserver.v1.account.domain.entity.Question;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

public class QuestionReadDto {

  @Data
  @Builder
  static public class Response{

    @ApiModelProperty(notes = "질문 id", example = "1")
    private Long questionId;

    @ApiModelProperty(notes = "질문 내용", example = "당신의 고향 이름은?")
    private String text;

    static public Response fromEntity(Question question){
      return Response.builder()
          .questionId(question.getQuestionId())
          .text(question.getText())
          .build();
    }
  }
}
