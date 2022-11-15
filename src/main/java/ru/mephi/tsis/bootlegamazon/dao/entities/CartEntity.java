package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "t_cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "cart_active")
    private Boolean active;

    public CartEntity() {
    }

    public CartEntity(Integer id, Integer userId, Boolean active) {
        this.id = id;
        this.userId = userId;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }
}
