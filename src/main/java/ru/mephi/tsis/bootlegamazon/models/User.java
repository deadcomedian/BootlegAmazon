package ru.mephi.tsis.bootlegamazon.models;

public class User {
    private String login;

    private String name;
    private String role;

    public User(String login, String name, String role) {
        this.login = login;
        this.name = name;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
