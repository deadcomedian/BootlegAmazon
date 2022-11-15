package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "tcl_status")
public class StatusEntity {

    @Id
    @Column(name = "status_id")
    private Integer id;

    @Column(name = "status_name")
    private String name;

    public StatusEntity() {
    }

    public StatusEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
