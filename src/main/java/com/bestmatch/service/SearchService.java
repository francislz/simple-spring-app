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
    private final TreeMap<Integer, List<Restaurant>> ratingMap = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<Double, List<Restaurant>> priceMap = new TreeMap<>();
    private final Map<String, List<Restaurant>> nameMap = new HashMap<>();
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
            ratingMap.computeIfAbsent(restaurant.getCustomerRating(), k -> new ArrayList<>()).add(restaurant);
            priceMap.computeIfAbsent(restaurant.getPrice(), k -> new ArrayList<>()).add(restaurant);
            nameMap.computeIfAbsent(restaurant.getName().toLowerCase(), k -> new ArrayList<>()).add(restaurant);
            cusineToRestaurantsMap.computeIfAbsent(restaurant.getCuisine(), k -> new ArrayList<>()).add(restaurant);
        }
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.getRestaurants();
    }

    public List<Cuisine> getAllCuisines() {
        return cuisineRepository.getCuisines();
    }

    public List<Restaurant> search(String name, Integer customerRating, Double distance, Double price, String cuisine) {
        List<Restaurant> restaurants = restaurantRepository.getRestaurants();
        Set<Restaurant> results = new HashSet<>(restaurantRepository.getRestaurants());

        if (name != null) {
            results.retainAll(restaurants.stream().filter(restaurant -> restaurant.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet()));
            // results.retainAll(nameMap.getOrDefault(name.toLowerCase(), Collections.emptyList()));
        }
        if (cuisine != null) {
            List<Cuisine> cuisines = cuisineRepository.filterCuisinesByName(cuisine);
            List<Restaurant> retainedRestaurants = new ArrayList<>();
            for (Cuisine c : cuisines) {
                retainedRestaurants.addAll(cusineToRestaurantsMap.getOrDefault(c.getId(), Collections.emptyList()));
            }
            results.retainAll(retainedRestaurants);
        }
        if (customerRating != null) {
            results.retainAll(ratingMap.tailMap(customerRating).values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        }
        if (distance != null) {
            results.retainAll(distanceMap.headMap(distance, true).values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        }
        if (price != null) {
            results.retainAll(priceMap.headMap(price, true).values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        }

        return results.stream()
                .sorted(Comparator.comparingDouble(Restaurant::getDistance)
                        .thenComparing(Comparator.comparingInt(Restaurant::getCustomerRating).reversed())
                        .thenComparingDouble(Restaurant::getPrice))
                .limit(5)
                .collect(Collectors.toList());
    }
}