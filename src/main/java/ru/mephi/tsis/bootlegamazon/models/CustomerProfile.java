package ru.mephi.tsis.bootlegamazon.models;

public class CustomerProfile {
    private String customerName;
    private String customerPhone;
    private String customerLogin;
    private String customerPassword;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerLogin() {
        return customerLogin;
    }

    public void setCustomerLogin(String customerLogin) {
        this.customerLogin = customerLogin;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public CustomerProfile(String customerName, String customerPhone, String customerLogin, String customerPassword) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerLogin = customerLogin;
        this.customerPassword = customerPassword;
    }
}
