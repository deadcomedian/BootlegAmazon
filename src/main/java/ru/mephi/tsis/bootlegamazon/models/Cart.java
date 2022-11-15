package ru.mephi.tsis.bootlegamazon.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Item> items;
    private double price;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Cart(ArrayList<Item> items, double price) {
        this.items = items;
        this.price = price;
    }
}
