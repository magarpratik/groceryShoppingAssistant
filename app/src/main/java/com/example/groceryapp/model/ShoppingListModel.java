package com.example.groceryapp.model;

import java.util.ArrayList;

public class ShoppingListModel {
    private String name;
    private ArrayList<ItemModel> list = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemModel> getList() {
        return list;
    }

    public void setList(ArrayList<ItemModel> list) {
        this.list = list;
    }
}
