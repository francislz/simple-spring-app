package com.bestmatch.controller;

import com.bestmatch.entity.*;
import com.bestmatch.service.SearchService;

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
		return searchService.search(name, customerRating, distance, price, cuisine);
	}

	@GetMapping("/all")
	@Operation(summary = "Get all restaurants", description = "Retrieves restaurants")
    public List<Restaurant> getAllRestaurants() {
        return searchService.getAllRestaurants();
    }

}
