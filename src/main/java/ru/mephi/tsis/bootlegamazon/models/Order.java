package ru.mephi.tsis.bootlegamazon.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Order {

    private Integer userId;
    private Integer orderNumber;
    private String orderStatus;
    private String orderAddress;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private double orderPrice;
    private String orderPaymentId;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderPaymentId() { return orderPaymentId; }

    public void setOrderPaymentId(String orderPaymentId) { this.orderPaymentId = orderPaymentId; }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Order(Integer userId, Integer orderNumber, String orderStatus, String orderAddress, LocalDate orderDate, double orderPrice, String orderPaymentId) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.orderPaymentId = orderPaymentId;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", orderDate=" + orderDate +
                ", orderPrice=" + orderPrice +
                ", orderPaymentId='" + orderPaymentId + '\'' +
                '}';
    }
}
