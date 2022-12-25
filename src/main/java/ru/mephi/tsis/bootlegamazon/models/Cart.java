package ru.mephi.tsis.bootlegamazon.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private Integer id;
    private ArrayList<CartArticle> items;

    private Map<Integer, Integer> itemsAmountMap;
    private Double price;

    public Cart(Integer id, List<CartArticle> items) {
        this.id = id;
        this.items = new ArrayList<>();
        this.items.addAll(items);
        double price = 0.0;
        this.itemsAmountMap = new HashMap<>();
        for (CartArticle item : items){
            price += item.getArticle().getPrice()*item.getAmount();
            this.itemsAmountMap.put(item.getArticle().getId(), item.getAmount());
        }
        this.price = price;
    }

    public Cart() {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getItemAmountByArticleId(Integer articleId){
        if (itemsAmountMap.get(articleId)!=null)
            return itemsAmountMap.get(articleId);
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", items=" + items +
                ", price=" + price +
                '}';
    }
}
