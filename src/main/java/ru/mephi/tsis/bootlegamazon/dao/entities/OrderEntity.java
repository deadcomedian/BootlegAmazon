package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "order_address")
    private String address;

    @Column(name = "order_date")
    private Date date;

    public OrderEntity() {
    }

    public OrderEntity(Integer id, Integer userId, Integer statusId, String address, Date date) {
        this.id = id;
        this.userId = userId;
        this.statusId = statusId;
        this.address = address;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getAddress() {
        return address;
    }

    public Date getDate() {
        return date;
    }
}
