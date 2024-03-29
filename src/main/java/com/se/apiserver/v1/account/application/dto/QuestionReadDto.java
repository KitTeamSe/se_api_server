package com.se.apiserver.v1.account.application.dto;

import com.se.apiserver.v1.account.domain.entity.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

public class QuestionReadDto {

  @Data
  @Builder
  @ApiModel("질문 조회 응답")
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

  @Data
  @Builder
  static public class ResponseWithAnswer{

    @ApiModelProperty(notes = "질문 id", example = "1")
    private Long questionId;

    @ApiModelProperty(notes = "질문 내용", example = "당신의 고향 이름은?")
    private String text;

    @ApiModelProperty(notes = "질문 정답", example = "구미")
    private String answer;

    static public ResponseWithAnswer fromEntity(Question question, String answer){
      return ResponseWithAnswer.builder()
          .questionId(question.getQuestionId())
          .text(question.getText())
          .answer(answer)
          .build();
    }
  }
}
