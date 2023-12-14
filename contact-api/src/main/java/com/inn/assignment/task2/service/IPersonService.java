package com.inn.assignment.task2.service;

import java.util.List;

import com.inn.assignment.task2.model.Person;

public interface IPersonService {
	Person create(Person person);

	Person update(Person person);

	List<Person> findAll();

	String delete(Integer id);

	List<Person> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType);

	Long getCount(String query);
}
