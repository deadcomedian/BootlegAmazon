package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(OrderArticleId.class)
@Table(name = "t_order_article", schema = "shop")
public class OrderArticleEntity {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "article_amount")
    private Integer articleAmount;

    @Column(name = "article_order_price")
    private Double articleOrderPrice;

    public OrderArticleEntity() {
    }

    public OrderArticleEntity(Integer orderId, Integer articleId, Integer articleAmount, Double articleOrderPrice) {
        this.orderId = orderId;
        this.articleId = articleId;
        this.articleAmount = articleAmount;
        this.articleOrderPrice = articleOrderPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getArticleAmount() {
        return articleAmount;
    }

    public Double getArticleOrderPrice() {
        return articleOrderPrice;
    }

}

