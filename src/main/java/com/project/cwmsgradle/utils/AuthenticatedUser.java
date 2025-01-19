package com.project.cwmsgradle.utils;

public class AuthenticatedUser {
    private static AuthenticatedUser instance;
    private String username;
    private String role;

    private AuthenticatedUser() {}

    public static AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }


    public String getRole() {
        return role;
    }


}