package com.inn.assignment.task2.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inn.assignment.task2.model.Contact;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "IContactController", description = "Endpoint for managing person data")
@Validated
public interface IContactController {

	@Operation(summary = "Check if the service is alive", description = "Returns a simple string to confirm that the service is running.")
	@GetMapping("/isAlive")
	public String isAlive();
	
	@Operation(summary = "Add a new Contact details in directory ", description = "Add a new Contact details in directory and return created Object in response")
	@PostMapping("/addContact")
	Contact addContact(@Valid @org.springframework.web.bind.annotation.RequestBody Contact model);
	
	@Operation(summary = "update a existing Contact details in directory", description = "update a existing details in directory and return updated data in response")
	@PostMapping("/editContact")
	Contact editContact(@Valid @org.springframework.web.bind.annotation.RequestBody Contact model);

	@Operation(summary = "Find all contact records", description = "Retrieve all the contact records.")
	@GetMapping("/findAll")
	public List<Contact> findAll();

	@Operation(summary = "Delete a existing Contact details from directory", description = "Delete a existing details in directory and return success")
	@DeleteMapping("/deleteContact/{id}")
	String deleteContact(@PathVariable Integer id);
	
	@Operation(summary = "Search contact record from contact directory", description = "Perform an advanced version of the search for contact records based on the provided query and limits.")
	@PostMapping(path = "/search")
	List<Contact> search(
			@RequestBody(content = @Content(schema = @Schema(implementation = String.class)), required = false) @Parameter(description = "Search query") String query,
			@RequestParam(required = false) @Parameter(description = "Upper limit for search") Integer ulimit,
			@RequestParam(required = false) @Parameter(description = "Lower limit for search") Integer llimit,
			@RequestParam(required = false) @Parameter(description = "Field to order results by") String orderBy,
			@RequestParam(required = false) @Parameter(description = "Type of ordering (ascending/descending") String orderType);

	@Hidden
	@Operation(summary = "Get count of searched contact records", description = "Retrieve the count of contact records based on the advanced search query.")
	@PostMapping(path = "getCount")
	Long getCount(
			@RequestBody(content = @Content(schema = @Schema(implementation = String.class)), required = false) @Parameter(description = "Search query") String query);
}
