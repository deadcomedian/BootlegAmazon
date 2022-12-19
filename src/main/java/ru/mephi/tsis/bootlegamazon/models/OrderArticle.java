package ru.mephi.tsis.bootlegamazon.models;

public class OrderArticle {

    private ArticleCard item;

    private Integer amount;

    private Double pricePerItem;

    public OrderArticle(ArticleCard item, Integer amount) {
        this.item = item;
        this.amount = amount;
        this.pricePerItem = item.getPrice();
    }

    public ArticleCard getItem() {
        return item;
    }

    public void setItem(ArticleCard item) {
        this.item = item;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return pricePerItem;
    }

    public void setPrice(Double price) {
        this.pricePerItem = price;
    }
}
