package com.inn.assignment.task2.core;

import lombok.experimental.UtilityClass;

public class Constant {
	public static final String EMPTY_OBJECT_STR = "{}";
	@UtilityClass
	public static final class ResponseKey {
		public static final String DATA = "data";
		public static final String STATUS = "status";
		public static final String STATUS_INFO = "statusInfo";
		public static final String STATUS_CODE = "statusCode";
		public static final String COUNT = "count";
		public static final String MESSAGE = "message";
		public static final String MEDIA_TYPE = "mediaType";
		public static final String ERROR = "error";
		public static final String HTTP_STATUS_CODE = "http.status_code";

	}
	
	@UtilityClass
	public static final class ResponseStatus {
		public static final String SUCCESS = "SUCCESS";
		public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
		public static final String NOT_IMPLEMENTED_SERVER_ERROR = "NOT_IMPLEMENTED_SERVER_ERROR";
		public static final String NO_CONTENT = "NO_CONTENT";
		public static final String INVALID_PATH_SEQUENCE = "INVALID_PATH_SEQUENCE";
		public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
		public static final String NOT_CREATE_DIRECTORY = "NOT_CREATE_DIRECTORY";
		public static final String FAILED = "FAILED";
		public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
		public static final String NOT_FOUND = "NOT_FOUND";
		public static final String SUCCESS_JSON = "{\"status\":\"success\"}";
		public static final String FAILURE_JSON = "{\"status\":\"failure\"}";
	}
	
	
	@UtilityClass
	public static final class FIQL {
		public static final String CONSTRAINT = "CONSTRAINT";
		public static final String TYPE = "type";
		public static final String SELECTOR = "selector";
		public static final String COMPARISON = "comparison";
		public static final String WILDCARD = "WILDCARD";
		public static final String ARGUMENT = "argument";
		public static final String INCLUDE = "INCLUDE";
		public static final String NOT_INCLUDE = "NOT_INCLUDE";
		public static final String COMBINATION = "COMBINATION";
		public static final String AND = "AND";
		public static final String OPERATOR = "operator";
		public static final String LHS = "lhs";
		public static final String RHS = "rhs";
		public static final String OR = "OR";
		public static final String OPERATOR_IS_INCORRECT = "Operator is incorrect";
		public static final String FIQL_TYPE_IS_INCORRECT = "Fiql type is incorrect";
	}
}