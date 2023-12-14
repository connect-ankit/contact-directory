package com.inn.assignment.task2.controller.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inn.assignment.task2.controller.IPersonController;
import com.inn.assignment.task2.model.Person;
import com.inn.assignment.task2.service.IPersonService;

import jakarta.validation.Valid;
@RestController
@RequestMapping(path = "/Person")
public class PersonControllerImpl implements IPersonController {

	@Autowired
	private IPersonService iPersonService;

	@Override
	public Person create(@Valid Person model) {
		return iPersonService.create(model);
	}

	@Override
	public Person update(@Valid Person model) {
		return iPersonService.update(model);
	}

	@Override
	public List<Person> findAll() {
		return iPersonService.findAll();
	}

	@Override
	public String delete(Integer id) {
		return iPersonService.delete(id);
	}

	@Override
	public List<Person> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType) {
		return iPersonService.search(query, ulimit, llimit, orderBy, orderType);
	}

	@Override
	public Long getCount(String query) {
		return iPersonService.getCount(query);
	}

}
