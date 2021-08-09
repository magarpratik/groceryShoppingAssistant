package com.example.groceryapp.model;

import android.icu.math.BigDecimal;

public class ItemModel {
    private int id;
    private String name;
    private String quantity;
    private String unit;
    private String pricePerUnit;
    private String price;
    private BigDecimal inStorePrice;
    private BigDecimal estimatedPrice;

    public ItemModel(String name) {
        this.name = name;
    }

    public ItemModel(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ItemModel(String name, String quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(String pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
