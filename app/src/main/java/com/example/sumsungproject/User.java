package com.example.sumsungproject;

public class User {
    public String id, telephone;
    public boolean is_admin;
    public int passwordHash;

    public User() {
    }

    public User(String id, String telephone, boolean is_admin, int passwordHash) {
        this.id = id;
        this.telephone = telephone;
        this.passwordHash = passwordHash;
        this.is_admin = is_admin;
    }
}
