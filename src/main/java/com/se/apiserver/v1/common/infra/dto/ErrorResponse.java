package com.se.apiserver.v1.common.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

  private int status;
  private String code;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<FieldError> errors = new ArrayList<>();

  @Builder
  public ErrorResponse(String message, String code, int status, List<FieldError> errors) {
    this.message = message;
    this.code = code;
    this.status = status;
    this.errors = initErrors(errors);
  }

  public ErrorResponse(String message, String code, int status) {
    this.message = message;
    this.code = code;
    this.status = status;
  }

  public static ErrorResponse of(ErrorCode error, BindingResult bindingResult) {
    return new ErrorResponse(error.getMessage(), error.getCode(), error.getStatus(), getFieldErrors(bindingResult));
  }

  public static ErrorResponse of(ErrorCode error) {
    return new ErrorResponse(error.getMessage(), error.getCode(), error.getStatus());
  }

  private List<FieldError> initErrors(List<FieldError> errors) {
    return (errors == null) ? new ArrayList<>() : errors;
  }

  private static List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
    final List<org.springframework.validation.FieldError> errors = bindingResult.getFieldErrors();
    return errors.parallelStream()
        .map(error -> ErrorResponse.FieldError.builder()
            .reason(error.getDefaultMessage())
            .field(error.getField())
            .value(getRejectedValue(error))
            .build())
        .collect(Collectors.toList());
  }

  private static String getRejectedValue(org.springframework.validation.FieldError error){
    Object rejectedValue = error.getRejectedValue();
    try{
      return String.valueOf(rejectedValue);
    }
    catch (Exception e){
      return "";
    }
  }

  @Getter
  public static class FieldError {

    private String field;
    private String value;
    private String reason;

    @Builder
    public FieldError(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }
  }

}
