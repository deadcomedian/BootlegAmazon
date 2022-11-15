package ru.mephi.tsis.bootlegamazon.dao.entities;

import java.io.Serializable;
import java.util.Objects;

public class CartArticleId implements Serializable {

    private Integer cartId;
    private Integer articleId;

    public CartArticleId() {
    }

    public CartArticleId(Integer cartId, Integer articleId) {
        this.cartId = cartId;
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartArticleId)) return false;
        CartArticleId that = (CartArticleId) o;
        return cartId.equals(that.cartId) && articleId.equals(that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, articleId);
    }
}
