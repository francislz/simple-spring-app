package com.bestmatch.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("search")
@Tag(name = "Search Controller", description = "Search data given the query parameters")
public class SearchController {
    @GetMapping("/")
	@Operation(summary = "Get a greeting message", description = "This endpoint returns a greeting message")
	String home() {
		return "Hello World!";
	}
}
