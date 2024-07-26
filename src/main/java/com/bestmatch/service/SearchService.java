package com.bestmatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestmatch.repository.RestaurantRepository;

import jakarta.annotation.PostConstruct;

import com.bestmatch.entity.Cuisine;
import com.bestmatch.entity.Restaurant;
import com.bestmatch.repository.CuisineRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final TreeMap<Double, List<Restaurant>> distanceMap = new TreeMap<>();
    private final TreeMap<Double, List<Restaurant>> priceMap = new TreeMap<>();
    private final Map<Integer, List<Restaurant>> cusineToRestaurantsMap = new HashMap<>();

    private final RestaurantRepository restaurantRepository;
    private final CuisineRepository cuisineRepository;

    @Autowired
    public SearchService(RestaurantRepository restaurantRepository, CuisineRepository cuisineRepository) {
        this.restaurantRepository = restaurantRepository;
        this.cuisineRepository = cuisineRepository;
    }

    @PostConstruct
    public void init() {
        for (Restaurant restaurant : restaurantRepository.getRestaurants()) {
            distanceMap.computeIfAbsent(restaurant.getDistance(), k -> new ArrayList<>()).add(restaurant);
            priceMap.computeIfAbsent(restaurant.getPrice(), k -> new ArrayList<>()).add(restaurant);
            cusineToRestaurantsMap.computeIfAbsent(restaurant.getCuisine(), k -> new ArrayList<>()).add(restaurant);
        }
    }

    private List<Restaurant> getSearchedCuisineRestaurantsOrAll(String cuisine) {
        if (cuisine == null) {
            return restaurantRepository.getRestaurants();
        } else {
            List<Cuisine> cuisines = cuisineRepository.filterCuisinesByName(cuisine);
            List<Restaurant> retainedRestaurants = new ArrayList<>();
            for (Cuisine c : cuisines) {
                retainedRestaurants.addAll(cusineToRestaurantsMap.getOrDefault(c.getId(), Collections.emptyList()));
            }
            return retainedRestaurants;
        }
    }

    private void applyRestaurantNameFilter(Set<Restaurant> restaurants, String name) {
        if (name != null) {
            restaurants.retainAll(restaurants.stream().filter(restaurant -> restaurant.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet()));
        }
    }

    private void applyCustomerRatingFilter(Set<Restaurant> restaurants, Integer customerRating) {
        if (customerRating != null) {
            restaurants.retainAll(restaurants.stream().filter(restaurant -> restaurant.getCustomerRating() >= customerRating).collect(Collectors.toSet()));
        }
    }

    private void applyDistanceFilter(Set<Restaurant> restaurants, Double distance) {
        if (distance != null) {
            restaurants.retainAll(restaurants.stream().filter(restaurant -> restaurant.getDistance() <= distance).collect(Collectors.toSet()));
        }
    }

    private void applyPriceFilter(Set<Restaurant> restaurants, Double price) {
        if (price != null) {
            restaurants.retainAll(restaurants.stream().filter(restaurant -> restaurant.getPrice() <= price).collect(Collectors.toSet()));
        }
    }

    public List<Restaurant> search(String name, Integer customerRating, Double distance, Double price, String cuisine) {
        List<Restaurant> restaurants = getSearchedCuisineRestaurantsOrAll(cuisine);
        Set<Restaurant> results = new HashSet<>(restaurants);

        applyRestaurantNameFilter(results, name);
        applyCustomerRatingFilter(results, customerRating);
        applyDistanceFilter(results, distance);
        applyPriceFilter(results, price);

        return results.stream()
                .sorted(Comparator.comparingDouble(Restaurant::getDistance)
                        .thenComparing(Comparator.comparingInt(Restaurant::getCustomerRating).reversed())
                        .thenComparingDouble(Restaurant::getPrice))
                .limit(5)
                .collect(Collectors.toList());
    }
}