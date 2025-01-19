package com.project.cwmsgradle.utils;

import com.project.cwmsgradle.entity.Client;

public class AuthenticatedUser {
    private static AuthenticatedUser instance;
    private String username;
    private String role;
    private Client client;

    private AuthenticatedUser() {
        // Initialize the client object here
        // For example, you can retrieve it from the database or set it manually
        this.client = new Client(); // Replace with actual initialization
    }

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

    public Client getClient() {
        return client;
    }

    public Long getClientId() {
        return client != null ? client.getClientId() : null;
    }
}