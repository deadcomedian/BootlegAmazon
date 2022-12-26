package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "t_cart", schema = "shop")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "cart_active")
    private Boolean active;

    public CartEntity() {
    }

    public CartEntity(Integer id, String userId, Boolean active) {
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

    public String getUserId() {
        return userId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
