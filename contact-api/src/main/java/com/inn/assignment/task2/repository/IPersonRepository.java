package com.inn.assignment.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inn.assignment.task2.model.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Integer> {

}
