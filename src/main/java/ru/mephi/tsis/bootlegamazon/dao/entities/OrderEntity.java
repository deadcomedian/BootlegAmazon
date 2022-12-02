package ru.mephi.tsis.bootlegamazon.dao.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_order", schema = "shop")
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "order_date")
    private LocalDate date;

    ////добавить !!!
    @Column(name = "payment_id")
    private String paymentId; // Накатить 1.4 БД, добавить строку с payment_id в таблицу заказов

    public OrderEntity() {
    }

    public OrderEntity(Integer id, Integer userId, Integer statusId, String address, LocalDate date, String paymentId) {
        this.id = id;
        this.userId = userId;
        this.statusId = statusId;
        this.address = address;
        this.date = date;
        this.paymentId = paymentId;
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

    public LocalDate getDate() {
        return date;
    }

    public String getPaymentId() { return paymentId; }
}
