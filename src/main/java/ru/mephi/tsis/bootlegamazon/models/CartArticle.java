package ru.mephi.tsis.bootlegamazon.models;

public class CartArticle {
    private ArticleCard article;
    private Integer amount;

    public CartArticle(ArticleCard article, Integer amount) {
        this.article = article;
        this.amount = amount;
    }


    public ArticleCard getArticle() {
        return article;
    }

    public void setArticle(ArticleCard article) {
        this.article = article;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
