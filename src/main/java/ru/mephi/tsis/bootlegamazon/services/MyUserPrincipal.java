package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;

import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private UserAuth user;

    public MyUserPrincipal(UserAuth user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserAuth getUser() {
        return user;
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }
}
