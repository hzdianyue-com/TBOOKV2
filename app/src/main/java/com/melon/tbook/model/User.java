package com.melon.tbook.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;

    public User(int id, String username, String password, String nickname, String email,String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.avatar = avatar;
    }
    public User(String username, String password, String nickname, String email, String avatar) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.avatar = avatar;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}