package com.bestmatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bestmatch.entity.Cuisine;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Collectors;

@Repository
public class CuisineRepository {
    private final List<Cuisine> cuisines = new ArrayList<>();
    private final Map<Integer, Cuisine> cuisineMap = new HashMap<>();

    private final CsvLoader loader;

    @Autowired
    public CuisineRepository(CsvLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    private void init() {
        cuisines.addAll(loader.loadCsv("cuisines.csv", Cuisine.class));
        for (Cuisine cuisine : cuisines) {
            cuisineMap.put(cuisine.getId(), cuisine);
        }
    }

    public List<Cuisine> filterCuisinesByName(String name) {
        List<Cuisine> filtered = cuisines.stream().filter(cuisine -> cuisine.getName().toLowerCase().contains(name.toLowerCase())).toList();
        if (filtered.isEmpty()) {
            return cuisines.stream().filter(cuisine -> cuisine.getName().toLowerCase().contains("other")).toList();
        }
        return filtered;
    }

    public Cuisine getCuisineById(int id) {
        return cuisineMap.get(id);
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }
}
