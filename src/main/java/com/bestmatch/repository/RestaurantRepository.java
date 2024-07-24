package com.bestmatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bestmatch.entity.Restaurant;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.ArrayList;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final CsvLoader loader;

    @Autowired
    public RestaurantRepository(CsvLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    private void init() {
        restaurants.addAll(loader.loadCsv("restaurants.csv", Restaurant.class));
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
