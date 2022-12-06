package ru.mephi.tsis.bootlegamazon.models;

public class User {
    private String login;
    private String password;
    private Integer roleId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public User(String login, String password, Integer roleId) {
        this.login = login;
        this.password = password;
        this.roleId = roleId;
    }
}
