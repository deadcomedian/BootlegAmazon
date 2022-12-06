package ru.mephi.tsis.bootlegamazon.dao.entities;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tcl_role", schema = "shop")
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String name;

    @OneToMany (mappedBy = "role", fetch=FetchType.EAGER)
    private Collection<UserAuth> users;

    public RoleEntity() {
    }

    public RoleEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Collection<UserAuth> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserAuth> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

}
