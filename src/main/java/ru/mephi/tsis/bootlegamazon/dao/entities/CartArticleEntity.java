package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;


@Entity
@IdClass(CartArticleId.class)
@Table(name = "t_cart_article")
public class CartArticleEntity {
    @Id
    @Column(name = "cart_id")
    private Integer cartId;

    @Id
    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "article_amount")
    private Integer articleAmount;

    public CartArticleEntity() {
    }

    public CartArticleEntity(Integer cartId, Integer articleId, Integer articleAmount) {
        this.cartId = cartId;
        this.articleId = articleId;
        this.articleAmount = articleAmount;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getArticleAmount() {
        return articleAmount;
    }

}

