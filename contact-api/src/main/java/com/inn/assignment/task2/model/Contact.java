package com.inn.assignment.task2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Valid
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CONTACT")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "Email Address is required")
	@Email(message = "Invalid email format")
	private String emailAddress;

	@NotEmpty(message = "Phone Number is required")
	@Size(min = 10, max = 10, message = "Phone Number should be of 10 digits only")
	@Pattern(regexp = "^[0-9]+$", message = "Phone Number should contain only digits")
	private String phoneNumber;

}
