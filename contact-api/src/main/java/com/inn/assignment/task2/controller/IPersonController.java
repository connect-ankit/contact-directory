package com.inn.assignment.task2.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inn.assignment.task2.model.Person;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
 
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Validated
@CrossOrigin(origins = "*")

@Tag(name = "IPersonController", description = "Endpoint for managing contact directory data")
public interface IPersonController {
	@Operation(summary = "Add a new person details in directory ", description = "Add a new person details in directory and return created Object in response")
	@PostMapping("/create")
	Person create(@Valid @RequestBody Person model);

	@Operation(summary = "update a existing person details in directory", description = "update a existing details and return updated data in response")
	@PostMapping("/update")
	Person update(@Valid @RequestBody Person model);

	@Operation(summary = "Find all person records", description = "Retrieve all the person records.")
	@GetMapping("/findAll")
	public List<Person> findAll();

	@Operation(summary = "Delete a existing person details from directory", description = "Delete a existing details in directory and return success")
	@DeleteMapping("/delete/{id}")
	String delete(@PathVariable Integer id);

	@Operation(summary = "Search person record from person directory", description = "Perform an advanced version of the search for person records based on the provided query and limits.")
	@PostMapping(path = "/search")
	List<Person> search(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = String.class)), required = false) @Parameter(description = "Search query") String query,
			@RequestParam(required = false) @Parameter(description = "Upper limit for search") Integer ulimit,
			@RequestParam(required = false) @Parameter(description = "Lower limit for search") Integer llimit,
			@RequestParam(required = false) @Parameter(description = "Field to order results by") String orderBy,
			@RequestParam(required = false) @Parameter(description = "Type of ordering (ascending/descending") String orderType);

	@Hidden
	@Operation(summary = "Get count of searched contact records", description = "Retrieve the count of contact records based on the advanced search query.")
	@PostMapping(path = "getCount")
	Long getCount(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = String.class)), required = false) @Parameter(description = "Search query") String query);
}
