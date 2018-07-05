package com.github.kkgy333.sword.auth.system.entity;

import java.io.Serializable;

public class JwtAuthEntity implements Serializable {


    private String username;
    private String password;


    public JwtAuthEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JwtAuthEntity() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}