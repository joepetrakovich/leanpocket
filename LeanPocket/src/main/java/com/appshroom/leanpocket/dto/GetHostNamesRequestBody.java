package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class GetHostNamesRequestBody {

    public String username;
    public String password;

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
}
