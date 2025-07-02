package com.example.quanlycv.model;

import java.io.Serializable;

public class User  implements Serializable {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String created_at;
    private String avatar_url;
    private String fullName;
    private String phoneNumber;

    public User(){

    }

    public User(int user_id, String username, String email, String password, String created_at, String avatar_url,String fullName,String phoneNumber) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.avatar_url = avatar_url;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
