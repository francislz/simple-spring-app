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
@Tag(name = "Search Restaurants", description = "Handles search operations")
public class SearchController {
	private final SearchService searchService;

	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

    @GetMapping("/")
	@Operation(summary = "Search the best restaurants", description = "Provided with restaurant name, distance, price, cuisine, and customer rating, return a list of restaurants that match the search criteria")
	public List<Restaurant> search(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Integer customerRating,
		@RequestParam(required = false) Double distance,
		@RequestParam(required = false) Double price,
		@RequestParam(required = false) String cuisine
	) {
		if (customerRating != null && (customerRating < 1 || customerRating > 5)) {
			throw new BadRequestException("Invalid customer rating, please provide a value between 1 and 5");
		}

		if (distance != null && (distance < 1 || distance > 10)) {
			throw new BadRequestException("Invalid distance, please provide a value between 1 and 10");
		}

		if (price != null && (price < 10 || price > 50)) {
			throw new BadRequestException("Invalid price, please provide a value between 10 and 50");
		}

		return searchService.search(name, customerRating, distance, price, cuisine);
	}

}
