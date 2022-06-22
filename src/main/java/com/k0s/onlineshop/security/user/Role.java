package com.k0s.onlineshop.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Data
@ToString
@AllArgsConstructor
public class Role implements GrantedAuthority {

    private long id;
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
