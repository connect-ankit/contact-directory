package com.inn.assignment.task2.core;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Basic;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {

  @JsonView(Basic.class)
  private Object data;

  @JsonView(Basic.class)
  private String status;

  @JsonView(Basic.class)
  private Integer statusCode;

  @JsonView(Basic.class)
  private String contentType;

  @JsonView(Basic.class)
  private Integer contentLength;

  private Object message;

  private Integer previousPage;

  private Integer currentPage;

  private Integer nextPage;

  private Integer currentPageCount;

  private Integer pageSize;

  private Integer totalCount;

  private Integer totalPage;

  GenericResponse(GenericResponseBuilder builder) {
    this.data = builder.getData();
    this.message = builder.getMessage();
    this.status = builder.getStatus();
    this.statusCode = builder.getStatusCode();
    this.contentType = builder.getContentType();
    this.contentLength = builder.getContentLength();
    this.currentPageCount = builder.getCurrentPageCount();
    this.pageSize = builder.getPageSize();
  }

  public static GenericResponseBuilder ok() {
    return GenericResponseBuilder.getInstance();
  }

  public static GenericResponseBuilder ok(List<?> data) {
    return ok().setData(data);
  }

 

}
