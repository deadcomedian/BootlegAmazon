package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(OrderArticleEntity.OrderArticleId.class)
@Table(name = "t_order_article")
public class OrderArticleEntity {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "article_amount")
    private Integer articleAmount;

    //????????
    @Column(name = "article_order_price")
    private Integer articleOrderPrice;

    public OrderArticleEntity() {
    }

    public OrderArticleEntity(Integer orderId, Integer articleId, Integer articleAmount, Integer articleOrderPrice) {
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

    public Integer getArticleOrderPrice() {
        return articleOrderPrice;
    }

    public static class OrderArticleId implements Serializable {

        private Integer orderId;
        private Integer articleId;

        public OrderArticleId() {
        }

        public OrderArticleId(Integer orderId, Integer articleId) {
            this.orderId = orderId;
            this.articleId = articleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderArticleId)) return false;
            OrderArticleId that = (OrderArticleId) o;
            return orderId.equals(that.orderId) && articleId.equals(that.articleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, articleId);
        }
    }
}
