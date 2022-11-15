package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CartArticleEntity.CartArticleId.class)
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

    public static class CartArticleId implements Serializable {

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
}
