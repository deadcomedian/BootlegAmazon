package ru.mephi.tsis.bootlegamazon.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private Integer id;
    private ArrayList<CartArticle> items;
    private Double price;

    public Cart(Integer id, List<CartArticle> items) {
        this.id = id;
        this.items = new ArrayList<>();
        this.items.addAll(items);
        double price = 0.0;
        for (CartArticle item : items){
            price += item.getArticle().getPrice()*item.getAmount();
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

    public Integer getId() {
        return id;
    }
}
