package com.inn.assignment.task2.service;

import org.json.JSONObject;

import com.inn.assignment.task2.core.GenericResponse;

public interface ISearchAndCountFilter {

	void addCriteria(String extraCriteria);

	GenericResponse search(Class<?> entity, String query, String orderBy, String orderType, Integer llimit,
			Integer ulimit);

	GenericResponse getResponse(Integer llimit, Integer ulimit, JSONObject data);

}
