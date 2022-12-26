package ru.mephi.tsis.bootlegamazon.models;

import javax.validation.constraints.*;
//import jakarta.validation.constraints.*;
//import org.springframework.validation.*;

public class Category {

    private Integer id;

    @NotNull
    @Size(min = 2, max = 20)
    @NotEmpty
    @NotBlank
    private String categoryName;

    public Category() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
