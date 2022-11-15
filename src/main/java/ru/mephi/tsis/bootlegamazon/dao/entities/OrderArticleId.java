package ru.mephi.tsis.bootlegamazon.dao.entities;

import java.io.Serializable;
import java.util.Objects;

public class OrderArticleId implements Serializable {

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
