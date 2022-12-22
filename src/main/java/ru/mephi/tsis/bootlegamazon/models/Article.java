package ru.mephi.tsis.bootlegamazon.models;

import javax.validation.constraints.*;

public class Article {

    private Integer id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min=2, max=20)
    private String itemName;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min=2, max=20)
    private String authorName;

    private String categoryName;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min=2, max=300)
    private String itemDescription;

    @Min(0)
    private double itemPrice;
    private double itemRating;
    private String itemPhoto;

    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article(Integer id, String itemName, String authorName, String categoryName, String itemDescription, double itemPrice, double itemRating, String itemPhoto, Integer amount) {
        this.id = id;
        this.itemName = itemName;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.itemPhoto = itemPhoto;
        this.amount = amount;
    }

    public Article() {
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemRating=" + itemRating +
                ", itemPhoto='" + itemPhoto + '\'' +
                ", amount=" + amount +
                '}';
    }
}
