package com.inn.assignment.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inn.assignment.task2.model.Contact;
@Repository
public interface IContactRepository extends JpaRepository<Contact, Integer> {
}
