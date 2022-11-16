package ru.mephi.tsis.bootlegamazon.models;

public class ArticleCard {

    private Integer id;

    private String itemName;
    private String authorName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public ArticleCard(Integer id, String itemName, String authorName) {
        this.id = id;
        this.itemName = itemName;
        this.authorName = authorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
