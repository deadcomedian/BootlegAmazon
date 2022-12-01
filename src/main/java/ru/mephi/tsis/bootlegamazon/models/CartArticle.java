package ru.mephi.tsis.bootlegamazon.models;

public class CartArticle {
    private Article article;
    private Integer amount;

    public CartArticle(Article article, Integer amount) {
        this.article = article;
        this.amount = amount;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
