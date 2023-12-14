package com.inn.assignment.task2.service;

import java.util.List;

import com.inn.assignment.task2.model.Contact;

public interface IContactService {

	Contact addContact(Contact model);

	Contact editContact(Contact model);

	List<Contact> findAll();

	String deleteContact(Integer id);

	List<Contact> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType);

	Long getCount(String query);
}
