package com.example.groceryapp.model;

import android.icu.math.BigDecimal;

public class ItemModel {
    private String name;
    private int weight;
    private int volume;
    private int quantity;
    private BigDecimal inStorePrice;
    private BigDecimal estimatedPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getInStorePrice() {
        return inStorePrice;
    }

    public void setInStorePrice(BigDecimal inStorePrice) {
        this.inStorePrice = inStorePrice;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
}
