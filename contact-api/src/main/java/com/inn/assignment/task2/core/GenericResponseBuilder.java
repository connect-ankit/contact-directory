package com.inn.assignment.task2.core;

import java.util.ArrayList;

 public class GenericResponseBuilder {

	private Object data;
	private Object message;
	private String status;
	private Integer statusCode;
	private String contentType;
	private Integer contentLength;
	private Integer currentPageCount;
	private Integer page;
	private Integer pageSize;
	private boolean search = false;

	public static final Integer OK = 200;
	public static final Integer CREATED = 201;
	public static final Integer NO_CONTENT = 204;
	public static final Integer NOT_FOUND = 404;
	public static final Integer BAD_REQUEST_ERROR = 400;
	public static final Integer UPGRADE_REQUIRED = 426;
	public static final Integer INTERNAL_SERVER_ERROR = 500;
	public static final Integer NOT_IMPLEMENTED_SERVER_ERROR = 501;
	public static final Integer UNAUTHORIZED_ERROR = 401;
	public static final Integer SERVICE_UNAVAILABLE = 503;
	public static final Integer GATEWAY_TIMEOUT = 504;
	public static final Integer ORIGIN_IS_UNREACHABLE = 523;
	public static final Integer CONNECTION_TIMED_OUT = 522;
	
	

	static GenericResponseBuilder getInstance() {
		return new GenericResponseBuilder();
	}

	public GenericResponseBuilder setData(Object data) {
		this.search = false;
		this.data = data;
		return this;
	}

	public Integer getCurrentPageCount() {
		return currentPageCount;
	}

	public void setCurrentPageCount(Integer currentPageCount) {
		this.currentPageCount = currentPageCount;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public Object getData() {
		return data;
	}

	public Object getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public Integer getContentLength() {
		return contentLength;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public GenericResponseBuilder setMessage(Object message) {
		this.message = message;
		return this;
	}

	public GenericResponseBuilder setSearchFilterData(Object data) {
		this.search = true;
		this.data = data;
		return this;
	}

	public GenericResponseBuilder setStatus(String status) {
		this.status = status;
		return this;
	}

	public GenericResponseBuilder setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public GenericResponseBuilder setPage(Integer page) {
		if (page != null) {
			this.page = page;
		}
		return this;
	}

	public GenericResponseBuilder setPageSize(Integer pageSize) {
		if (this.page != null) {
			this.pageSize = pageSize;
		}
		return this;
	}

	public GenericResponseBuilder setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	public GenericResponseBuilder setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	GenericResponseBuilder noContent() {
		this.data = new ArrayList<>();
		this.status = Constant.ResponseStatus.NO_CONTENT;
		this.statusCode = NO_CONTENT;
		return this;
	}

	
	public GenericResponse build() {
		
		return new GenericResponse(this);
	}
}
