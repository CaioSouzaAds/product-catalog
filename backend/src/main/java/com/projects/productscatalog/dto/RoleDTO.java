package com.projects.productscatalog.dto;

import java.io.Serializable;

import com.projects.productscatalog.entities.Role;

public class RoleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String authority;

    
    public RoleDTO() {
    }

    
    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    
    public RoleDTO(Role role) {
        this(role.getId(), role.getAuthority());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    
}
