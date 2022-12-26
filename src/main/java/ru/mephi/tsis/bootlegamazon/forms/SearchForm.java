package ru.mephi.tsis.bootlegamazon.forms;

public class SearchForm {
    private String searchField;

    public SearchForm() {
    }

    public SearchForm(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }
}
