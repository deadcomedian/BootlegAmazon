package ru.mephi.tsis.bootlegamazon.models;

public class ItemCard {
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

    public ItemCard(String itemName, String authorName) {
        this.itemName = itemName;
        this.authorName = authorName;
    }
}
