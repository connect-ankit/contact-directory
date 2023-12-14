package com.inn.assignment.task2.controller.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.assignment.task2.controller.IContactController;
import com.inn.assignment.task2.core.BusinessException;
import com.inn.assignment.task2.model.Contact;
import com.inn.assignment.task2.service.IContactService;

import io.swagger.v3.oas.annotations.media.Content;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/Contact")
public class ContactControllerImpl implements IContactController {

	@Autowired
	IContactService contactService;

	@Override
	public String isAlive() {
		return "Service is alive";
	}

	@Override
	public Contact addContact(@Valid Contact model) {
		return contactService.addContact(model);
	}

	@Override
	public Contact editContact(@Valid Contact model) {
		
		return contactService.addContact(model);
	}

	@Override
	public List<Contact> findAll() {
		return contactService.findAll();
	}

	@Override
	public String deleteContact(Integer id) {
		try {
			return contactService.deleteContact(id);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException(e.getMessage());
		}
		
	}

	@Override
	public List<Contact> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType) {
		return contactService.search(query, ulimit, llimit, orderBy, orderType);
	}

	@Override
	public Long getCount(String query) {
		return contactService.getCount(query);
	}


}
