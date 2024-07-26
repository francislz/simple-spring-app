package com.bestmatch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        List<Restaurant> result = searchService.search("Mcdonald", null, null, null, null);
        assertEquals(1, result.size());
        assertEquals("Mcdonald's", result.get(0).getName());
    }

    @Test
    public void testSearchByCustomerRating() {
        List<Restaurant> result = searchService.search(null, 4, null, null, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(restaurants.get(0))); // Mcdonald's
        assertTrue(result.contains(restaurants.get(2))); // Burger King
    }

    @Test
    public void testSearchByDistance() {
        List<Restaurant> result = searchService.search(null, null, 1.5, null, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(restaurants.get(1))); // KFC
        assertTrue(result.contains(restaurants.get(2))); // Burger King
    }

    @Test
    public void testSearchByPrice() {
        List<Restaurant> result = searchService.search(null, null, null, 15.0, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(restaurants.get(0))); // Mcdonald's
        assertTrue(result.contains(restaurants.get(1))); // KFC
    }

    @Test
    public void testSearchByCuisine() {
        when(cuisineRepository.filterCuisinesByName("American")).thenReturn(Collections.singletonList(cuisines.get(0)));
        List<Restaurant> result = searchService.search(null, null, null, null, "American");
        assertEquals(1, result.size());
        assertEquals("Mcdonald's", result.get(0).getName());
    }

    @Test
    public void testSearchByMultipleCriteria() {
        when(cuisineRepository.filterCuisinesByName("Burgers")).thenReturn(Collections.singletonList(cuisines.get(2)));
        List<Restaurant> result = searchService.search("King", 5, 2.0, 25.0, "Burgers");
        assertEquals(1, result.size());
        assertEquals("Burger King", result.get(0).getName());
    }
}