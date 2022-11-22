package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "t_user", schema = "shop")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_phone")
    private String phone;

    @Column(name = "user_login")
    private String login;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "user_active")
    private Boolean active;

    public UserEntity() {
    }

    public UserEntity(Integer id, String name, String phone, String login, String password, Integer roleId, Boolean active) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.roleId = roleId;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
