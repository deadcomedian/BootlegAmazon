package ru.mephi.tsis.bootlegamazon.models;

public class ArticleCard {

    private Integer id;

    private String itemName;
    private String authorName;

    private String photo;

    private Double price;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public ArticleCard(Integer id, String itemName, String authorName, String photo, Double price) {
        this.id = id;
        this.itemName = itemName;
        this.authorName = authorName;
        this.photo = photo;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
