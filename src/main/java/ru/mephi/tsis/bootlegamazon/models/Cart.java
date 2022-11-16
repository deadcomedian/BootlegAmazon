package ru.mephi.tsis.bootlegamazon.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<ArticleCard> items;
    private Double price;

    public Cart(ArrayList<ArticleCard> items, Double price) {
        this.items = items;
        this.price = price;
    }

    public ArrayList<ArticleCard> getItems() {
        return items;
    }

    public void setItems(ArrayList<ArticleCard> items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
