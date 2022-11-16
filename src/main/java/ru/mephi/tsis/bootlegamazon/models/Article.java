package ru.mephi.tsis.bootlegamazon.models;

public class Article {

    private Integer id;
    private String itemName;
    private String authorName;
    private String categoryName;
    private String itemDescription;
    private double itemPrice;
    private double itemRating;
    private String itemPhoto;
    private boolean itemActive;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemRating() {
        return itemRating;
    }

    public void setItemRating(double itemRating) {
        this.itemRating = itemRating;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public void setItemPhoto(String itemPhoto) {
        this.itemPhoto = itemPhoto;
    }

    public boolean isItemActive() {
        return itemActive;
    }

    public void setItemActive(boolean itemActive) {
        this.itemActive = itemActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article(Integer id, String itemName, String authorName, String categoryName, String itemDescription, double itemPrice, double itemRating, String itemPhoto, boolean itemActive) {
        this.id = id;
        this.itemName = itemName;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.itemPhoto = itemPhoto;
        this.itemActive = itemActive;
    }
}
