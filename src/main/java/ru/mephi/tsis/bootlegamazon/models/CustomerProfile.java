package ru.mephi.tsis.bootlegamazon.models;

public class CustomerProfile {
    private String customerName;
    private String customerPhone;
    private String customerLogin;
    private String customerPassword;
    private String customerRole;

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

    public String getCustomerRole() {
        return customerRole;
    }

    public void setCustomerRole(String customerRole) {
        this.customerRole = customerRole;
    }

    public CustomerProfile(String customerName, String customerPhone, String customerLogin, String customerPassword, String customerRole) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerLogin = customerLogin;
        this.customerPassword = customerPassword;
        this.customerRole = customerRole;
    }
}
