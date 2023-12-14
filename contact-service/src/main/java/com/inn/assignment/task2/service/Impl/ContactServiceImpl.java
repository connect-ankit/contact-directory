package com.inn.assignment.task2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.assignment.task2.core.BusinessException;
import com.inn.assignment.task2.core.Constant;
import com.inn.assignment.task2.core.GenericResponse;
import com.inn.assignment.task2.core.Utils;
import com.inn.assignment.task2.model.Contact;
import com.inn.assignment.task2.repository.IContactRepository;
import com.inn.assignment.task2.service.IContactService;
import com.inn.assignment.task2.service.ISearchAndCountFilter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
public class ContactServiceImpl implements IContactService {

	@Autowired
	IContactRepository contactRepository;
	@Autowired
	private ISearchAndCountFilter searchFilterService;
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	@Transactional
	public Contact addContact(Contact contact) { 
		if (contact.getId() == null) {
             return contactRepository.save(contact);
        } else {
            entityManager.merge(contact);
            return contact;
        }
	   }

	@Override
	public Contact editContact(Contact model) {

		return contactRepository.saveAndFlush(model);
	}

	@Override
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}

	@Override
	public String deleteContact(Integer id) {
		try {
			Contact contact = contactRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("No record available by ID: " + id));
			;
			contactRepository.delete(contact);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return Constant.ResponseStatus.SUCCESS_JSON;
	}

	private List<Contact> convertSearchData(GenericResponse genericResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Contact> entityList = mapper.readValue(genericResponse.getData().toString(),
					new TypeReference<List<Contact>>() {
					});
			return entityList;
		} catch (JsonMappingException e) {
			throw new BusinessException(e.getLocalizedMessage());
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<Contact> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType) {
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(query) && !Constant.EMPTY_OBJECT_STR.equals(query)) {
			query = Utils.getFiql(query);
		}
		GenericResponse genericResponse = searchFilterService.search(Contact.class, query, orderBy, orderType, llimit,
				ulimit);
		return convertSearchData(genericResponse);
	}

	@Override
	public Long getCount(String query) {
		return null;
	}

}
