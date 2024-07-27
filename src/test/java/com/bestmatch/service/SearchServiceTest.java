package com.bestmatch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bestmatch.dtos.RestaurantResponseDTO;
import com.bestmatch.entity.Cuisine;
import com.bestmatch.entity.Restaurant;
import com.bestmatch.repository.CuisineRepository;
import com.bestmatch.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CuisineRepository cuisineRepository;

    @InjectMocks
    private SearchService searchService;

    private List<Restaurant> restaurants;
    private List<Cuisine> cuisines;

    @BeforeEach
    public void setUp() {
        Restaurant r1 = new Restaurant("Mcdonald's", 4, 2.0, 10.0, 1);
        Restaurant r2 = new Restaurant("KFC", 3, 1.5, 15.0, 2);
        Restaurant r3 = new Restaurant("Burger King", 5, 1.0, 20.0, 3);
        
        restaurants = Arrays.asList(r1, r2, r3);

        Cuisine c1 = new Cuisine(1, "American");
        Cuisine c2 = new Cuisine(2, "Fast Food");
        Cuisine c3 = new Cuisine(3, "Burgers");
        
        cuisines = Arrays.asList(c1, c2, c3);

        when(restaurantRepository.getRestaurants()).thenReturn(restaurants);

        searchService.init();
    }

    @Test
    public void testSearchByName() {
        // Arrange
        when(cuisineRepository.getCuisineById(1)).thenReturn(cuisines.get(0));
        // Act
        List<RestaurantResponseDTO> result = searchService.search("Mcdonald", null, null, null, null);
        // Assert
        assertEquals(1, result.size());
        assertEquals("Mcdonald's", result.get(0).getName());
    }

    @Test
    public void testSearchByCustomerRating() {
        // Arrange
        when(cuisineRepository.getCuisineById(1)).thenReturn(cuisines.get(0));
        when(cuisineRepository.getCuisineById(3)).thenReturn(cuisines.get(2));
        // Act
        List<RestaurantResponseDTO> result = searchService.search(null, 4, null, null, null);
        // Assert
        assertEquals(2, result.size());
        RestaurantResponseDTO expected1 = RestaurantResponseDTO.fromRestaurant(restaurants.get(2), cuisines.get(2));
        assertEquals(result.get(0).getName(), expected1.getName());
        RestaurantResponseDTO expected2 = RestaurantResponseDTO.fromRestaurant(restaurants.get(0), cuisines.get(0));
        assertEquals(result.get(1).getName(), expected2.getName());
    }

    @Test
    public void testSearchByDistance() {
        // Arrange
        when(cuisineRepository.getCuisineById(2)).thenReturn(cuisines.get(1));
        when(cuisineRepository.getCuisineById(3)).thenReturn(cuisines.get(2));
        // Act
        List<RestaurantResponseDTO> result = searchService.search(null, null, 1.5, null, null);
        // Assert
        assertEquals(2, result.size());
        RestaurantResponseDTO expected1 = RestaurantResponseDTO.fromRestaurant(restaurants.get(2), cuisines.get(2));
        assertEquals(result.get(0).getName(), expected1.getName());
        RestaurantResponseDTO expected2 = RestaurantResponseDTO.fromRestaurant(restaurants.get(1), cuisines.get(1));
        assertEquals(result.get(1).getName(), expected2.getName());
    }

    @Test
    public void testSearchByPrice() {
        // Arrange
        when(cuisineRepository.getCuisineById(1)).thenReturn(cuisines.get(0));
        when(cuisineRepository.getCuisineById(2)).thenReturn(cuisines.get(1));
        // Act
        List<RestaurantResponseDTO> result = searchService.search(null, null, null, 15.0, null);
        // Assert
        assertEquals(2, result.size());
        RestaurantResponseDTO expected1 = RestaurantResponseDTO.fromRestaurant(restaurants.get(1), cuisines.get(1));
        assertEquals(result.get(0).getName(), expected1.getName());
        RestaurantResponseDTO expected2 = RestaurantResponseDTO.fromRestaurant(restaurants.get(0), cuisines.get(0));
        assertEquals(result.get(1).getName(), expected2.getName());
    }

    @Test
    public void testSearchByCuisine() {
        // Arrange
        when(cuisineRepository.getCuisineById(1)).thenReturn(cuisines.get(0));
        when(cuisineRepository.filterCuisinesByName("American")).thenReturn(Collections.singletonList(cuisines.get(0)));
        // Act
        List<RestaurantResponseDTO> result = searchService.search(null, null, null, null, "American");
        // Assert
        assertEquals(1, result.size());
        assertEquals("Mcdonald's", result.get(0).getName());
    }

    @Test
    public void testSearchByMultipleCriteria() {
        // Arrange
        when(cuisineRepository.getCuisineById(3)).thenReturn(cuisines.get(2));
        when(cuisineRepository.filterCuisinesByName("Burgers")).thenReturn(Collections.singletonList(cuisines.get(2)));
        // Act
        List<RestaurantResponseDTO> result = searchService.search("King", 5, 2.0, 25.0, "Burgers");
        // Assert
        assertEquals(1, result.size());
        assertEquals("Burger King", result.get(0).getName());
    }
}