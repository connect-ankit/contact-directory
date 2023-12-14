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
import com.inn.assignment.task2.model.Person;
import com.inn.assignment.task2.repository.IPersonRepository;
import com.inn.assignment.task2.service.IPersonService;
import com.inn.assignment.task2.service.ISearchAndCountFilter;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private IPersonRepository personRepository;
	
	@Autowired
	private ISearchAndCountFilter searchFilterService;

	@Override
	@Transactional
	public Person create(Person person) {
		
		try {
			return personRepository.save(person);
		} catch (Exception e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
		
	}

	@Override
	public Person update(Person person) {
		return personRepository.save(person);
	}

	@Override
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	@Override
	public String delete(Integer id) {
		try {
			Person person = personRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("No record available by ID: " + id));
			;
			personRepository.delete(person);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return Constant.ResponseStatus.SUCCESS_JSON;
	}

	private List<Person> convertSearchData(GenericResponse genericResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Person> entityList = mapper.readValue(genericResponse.getData().toString(),
					new TypeReference<List<Person>>() {
					});
			return entityList;
		} catch (JsonMappingException e) {
			throw new BusinessException(e.getLocalizedMessage());
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<Person> search(String query, Integer ulimit, Integer llimit, String orderBy, String orderType) {
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(query) && !Constant.EMPTY_OBJECT_STR.equals(query)) {
			query = Utils.getFiql(query);
		}
		GenericResponse genericResponse = searchFilterService.search(Person.class, query, orderBy, orderType, llimit,
				ulimit);
		return convertSearchData(genericResponse);
	}

	@Override
	public Long getCount(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
