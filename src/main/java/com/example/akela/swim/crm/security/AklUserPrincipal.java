package com.example.akela.swim.crm.security;

import com.example.akela.swim.crm.user.AklUser;
import com.example.akela.swim.crm.user.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AklUserPrincipal implements UserDetails {

    private final AklUser user;

    public AklUserPrincipal(AklUser user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getRole() {
        return user.getRole().name();
    }

    public Long getCoachId() {
        return user.getCoachId();
    }

    public UserStatus getStatus() {
        return user.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security expects ROLE_ prefix for roles
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.DISABLED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // ACTIVE si PENDING_PASSWORD trebuie sa fie "enabled"
        // DISABLED trebuie sa fie false
        return user.getStatus() != UserStatus.DISABLED;
    }
}
