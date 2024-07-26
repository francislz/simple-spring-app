package com.bestmatch.controller;

import com.bestmatch.entity.*;
import com.bestmatch.service.SearchService;
import com.bestmatch.exception.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("search")
@Tag(name = "Search Controller", description = "Search data given the query parameters")
public class SearchController {
	private final SearchService searchService;

	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

    @GetMapping("/")
	@Operation(summary = "Search the best restaurants", description = "Searches")
	public List<Restaurant> search(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Integer customerRating,
		@RequestParam(required = false) Double distance,
		@RequestParam(required = false) Double price,
		@RequestParam(required = false) String cuisine
	) {
		// Validate query parameters based on:
		// Restaurant Name, Customer Rating(1 star ~ 5 stars), 
		// Distance(1 mile ~ 10 miles), 
		// Price(how much one person will spend on average, $10 ~ $50), 
		// Cuisine(Chinese, American, Thai, etc.)

		if (customerRating != null && (customerRating < 1 || customerRating > 5)) {
			// Return 400 Bad Request if customerRating is not between 1 and 5
			throw new BadRequestException("Invalid customer rating");
		}

		if (distance != null && (distance < 1 || distance > 10)) {
			throw new BadRequestException("Invalid distance");
		}

		if (price != null && (price < 10 || price > 50)) {
			throw new BadRequestException("Invalid price");
		}

		return searchService.search(name, customerRating, distance, price, cuisine);
	}

}
