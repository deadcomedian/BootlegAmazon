package ru.mephi.tsis.bootlegamazon.models;

public class Invoice {
    private Integer id;
    private ArticleCard articleCard;
    private Integer articleCount;


    public Invoice(Integer id, ArticleCard articleCard, Integer articleCount) {
        this.id = id;
        this.articleCard = articleCard;
        this.articleCount = articleCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArticleCard getArticleCard() {
        return articleCard;
    }

    public void setArticleCard(ArticleCard articleCard) {
        this.articleCard = articleCard;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }
}
