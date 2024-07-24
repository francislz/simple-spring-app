package com.bestmatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bestmatch.entity.Cuisine;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.ArrayList;

@Repository
public class CuisineRepository {
    private final List<Cuisine> cuisines = new ArrayList<>();

    private final CsvLoader loader;

    @Autowired
    public CuisineRepository(CsvLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    private void init() {
        cuisines.addAll(loader.loadCsv("cuisines.csv", Cuisine.class));
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }
}
