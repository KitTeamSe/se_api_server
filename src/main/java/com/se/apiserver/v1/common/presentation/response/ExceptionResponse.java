package com.se.apiserver.v1.common.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class ExceptionResponse {

  private String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<FieldError> errors = new ArrayList<>();

  public ExceptionResponse(Exception e, List<FieldError> errors) {
    this.message = e.getMessage();
    this.errors = initErrors(errors);
  }

  public ExceptionResponse(Exception e) {
    this.message = e.getMessage();
  }

  public static ExceptionResponse of(Exception e, BindingResult bindingResult) {
    return new ExceptionResponse(e, getFieldErrors(bindingResult));
  }

  public static ExceptionResponse of(Exception e) {
    return new ExceptionResponse(e);
  }

  public static ExceptionResponse of(Exception e, String message) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(e);
    exceptionResponse.message = message;
    return exceptionResponse;
  }

  private List<FieldError> initErrors(List<FieldError> errors) {
    return (errors == null) ? new ArrayList<>() : errors;
  }

  private static List<ExceptionResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
    final List<org.springframework.validation.FieldError> errors = bindingResult.getFieldErrors();
    return errors.parallelStream()
        .map(error ->
            new FieldError(error.getField(), getRejectedValue(error), error.getDefaultMessage()))
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

  public String getMessage() {
    return message;
  }

  public List<FieldError> getErrors() {
    return errors;
  }

  public static class FieldError {

    private String field;
    private String value;
    private String reason;

    public FieldError(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public String getField() {
      return field;
    }

    public String getValue() {
      return value;
    }

    public String getReason() {
      return reason;
    }
  }
}