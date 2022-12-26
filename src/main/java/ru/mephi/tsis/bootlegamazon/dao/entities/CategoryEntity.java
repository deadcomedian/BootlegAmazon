package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "tcl_category", schema = "shop")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_active")
    private Boolean active;

    public CategoryEntity() {
    }

    public CategoryEntity(Integer id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
