package com.bestmatch.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bestmatch.entity.Restaurant;
import com.bestmatch.exception.BadRequestException;
import com.bestmatch.service.SearchService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    private List<Restaurant> restaurants;

    @BeforeEach
    public void setUp() {
        Restaurant r1 = new Restaurant("Mcdonald's", 4, 2.0, 10.0, 1);
        Restaurant r2 = new Restaurant("KFC", 3, 1.5, 15.0, 2);
        Restaurant r3 = new Restaurant("Burger King", 5, 1.0, 20.0, 3);
        
        restaurants = Arrays.asList(r1, r2, r3);

        searchService.init();
    }

    @Test
    public void testCustomerRatingValidation() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> searchController.search(null, 0, null, null, null));
        assertEquals("Invalid customer rating, please provide a value between 1 and 5", exception.getMessage());
        BadRequestException exception2 = assertThrows(BadRequestException.class, () -> searchController.search(null, 6, null, null, null));
        assertEquals("Invalid customer rating, please provide a value between 1 and 5", exception2.getMessage());
    }

    @Test
    public void testDistanceValidation() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> searchController.search(null, null, 0.0, null, null));
        assertEquals("Invalid distance, please provide a value between 1 and 10", exception.getMessage());
        BadRequestException exception2 = assertThrows(BadRequestException.class, () -> searchController.search(null, null, 11.0, null, null));
        assertEquals("Invalid distance, please provide a value between 1 and 10", exception2.getMessage());
    }

    @Test
    public void testPriceValidation() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> searchController.search(null, null, null, 200.0, null));
        assertEquals("Invalid price, please provide a value between 10 and 50", exception.getMessage());
        BadRequestException exception2 = assertThrows(BadRequestException.class, () -> searchController.search(null, null, null, 9.0, null));
        assertEquals("Invalid price, please provide a value between 10 and 50", exception2.getMessage());
    }

    @Test
    public void testSearchRestaurants(){
        // Arrange
        when(searchService.search(null, null, null, null, null)).thenReturn(restaurants);
        // Act
        List<Restaurant> result = searchController.search(null, null, null, null, null);
        // Assert
        assertEquals(3, result.size());
        assertEquals("Mcdonald's", result.get(0).getName());
        assertEquals("KFC", result.get(1).getName());
        assertEquals("Burger King", result.get(2).getName());
    }
}
