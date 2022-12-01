package ru.mephi.tsis.bootlegamazon.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CartArticle> items;
    private Double price;

    public Cart(ArrayList<CartArticle> items) {
        this.items = items;
        double price = 0.0;
        for (CartArticle item : items){
            price += item.getArticle().getItemPrice()*item.getAmount();
        }
        this.price = price;
    }

    public ArrayList<CartArticle> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartArticle> items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
