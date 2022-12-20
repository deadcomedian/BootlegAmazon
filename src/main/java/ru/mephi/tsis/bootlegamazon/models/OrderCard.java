package ru.mephi.tsis.bootlegamazon.models;

public class OrderCard {

    private Integer number;

    private String username;

    private String status;

    private Double price;

    public OrderCard(Integer number, String username, String status, Double price) {
        this.number = number;
        this.username = username;
        this.status = status;
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public Double getPrice() {
        return price;
    }
}
