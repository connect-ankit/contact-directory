package com.inn.assignment.task2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Valid
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERSON")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
	@NotEmpty(message = "Name is required")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Name cannot have numbers or special characters")
	private String name;

	@Valid
	@OneToOne(targetEntity = Contact.class, fetch = jakarta.persistence.FetchType.EAGER, cascade = CascadeType.ALL)
	private Contact contact;

}
