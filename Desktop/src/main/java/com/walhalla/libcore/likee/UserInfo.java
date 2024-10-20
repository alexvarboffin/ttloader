package com.walhalla.libcore.likee;

public class UserInfo {
    public String userName;
    public long userId;

    public UserInfo(String userName, long userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public UserInfo(String userName) {
        this.userName = userName;
    }
}
