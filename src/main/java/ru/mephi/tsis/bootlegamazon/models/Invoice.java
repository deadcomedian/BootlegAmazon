package ru.mephi.tsis.bootlegamazon.models;

public class Invoice {
    Integer id;
    Integer articleId;
    Integer articleCount;

    public Invoice(Integer id, Integer articleId, Integer articleCount) {
        this.id = id;
        this.articleId = articleId;
        this.articleCount = articleCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }
}
