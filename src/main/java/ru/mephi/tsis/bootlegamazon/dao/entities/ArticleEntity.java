package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table (name = "t_article", schema = "shop")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer id;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "article_name")
    private String name;

    @Column(name = "article_author")
    private String author;

    @Column(name = "article_description")
    private String description;

    @Column(name = "article_photo")
    private String photo;

    @Column(name = "article_price")
    private Double price;

    @Column(name = "article_amount")
    private Integer amount;

    @Column(name = "article_active")
    private Boolean active;;

    @Column(name = "article_rating")
    private Double rating;

    public ArticleEntity() {
    }

    public ArticleEntity(Integer id, Integer categoryId, String name, String author, String description, String photo, Double price, Integer amount, Boolean active, Double rating) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.amount = amount;
        this.active = active;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public Boolean getActive() {
        return active;
    }

    public Double getRating() {
        return rating;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
