package com.example.groceryapp.model;

import java.util.ArrayList;

public class  ShoppingListModel {
    private int id;
    private String name;
    private String store = "";
    private ArrayList<ItemModel> list = new ArrayList<>();

    public ShoppingListModel (String name) {
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getStore() { return store; }

    public void setStore(String store) { this.store = store; }

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
