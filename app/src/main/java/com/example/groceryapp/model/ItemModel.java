package com.example.groceryapp.model;

import android.icu.math.BigDecimal;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.math.RoundingMode;

public class ItemModel implements Serializable {
    boolean isCrossed = false;
    private int listId;
    private int id;
    private String name;
    private String quantity;
    private String unit;
    private String pricePerUnit;
    private String price;
    private int storeId;
    private BigDecimal inStorePrice;
    private BigDecimal estimatedPrice;
    private String extractedPPU;
    private String weight;
    private int realQuantity;

    public ItemModel(int listId, String name) {
        this.listId = listId;
        this.name = name;
    }

    public ItemModel(int listId, String name, String quantity, String unit) {
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(int realQuantity) {
        this.realQuantity = realQuantity;
    }

    public String getExtractedPPU() {
        return extractedPPU;
    }

    public void setExtractedPPU(String extractedPPU) {
        this.extractedPPU = extractedPPU;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setCrossed(boolean crossed) {
        isCrossed = crossed;
    }

    public int getCrossedValue() {
        int ans = 0;
        if (isCrossed) {
            ans = 1;
        }
        return ans;
    }

    public void setCrossedValue(int isCrossed) {
        if (isCrossed == 0) {
            setCrossed(false);
        }
        else setCrossed(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void extractPPU(String pricePerUnit) {
        String[] result = pricePerUnit.split("/");
        String res = null;
        if(result[0].contains("p")) {
            StringBuilder num = new StringBuilder();
            num.append(result[0]);
            num.setLength(num.length() - 1);

            BigDecimal bigDecimal = new BigDecimal(num.toString());
            BigDecimal divisor = new BigDecimal("100");
            BigDecimal answer = bigDecimal.divide(divisor, 4, BigDecimal.ROUND_HALF_EVEN);
            res = answer.toString();
        }
        else if(result[0].contains("£")) {
            StringBuilder num = new StringBuilder();
            num.append(result[0]);
            num.delete(0, 1);
            res = num.toString();
        }
        else {
            res = result[0];
        }
        setExtractedPPU(res);
    }
}
