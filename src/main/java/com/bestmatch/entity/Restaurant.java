package com.bestmatch.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant implements Serializable {
    private String name;
    private int customerRating;
    private double distance;
    private double price;
    private int cuisineId;

    public Restaurant() {
    }

    public Restaurant(String name, int customerRating, double distance, double price, Integer cuisineId) {
        this.name = name;
        this.customerRating = customerRating;
        this.distance = distance;
        this.price = price;
        this.cuisineId = cuisineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(int customerRating) {
        this.customerRating = customerRating;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getCuisine() {
        return cuisineId;
    }

    public void setCuisine(Integer cuisine) {
        this.cuisineId = cuisine;
    }
}