package com.project.cwmsgradle.utils;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.User;

/**
 * Klasa reprezentująca uwierzytelnionego użytkownika.
 */
public class AuthenticatedUser {
    private static AuthenticatedUser instance;
    private String username;
    private String role;
    private Long userId;
    private Client client;

    /**
     * Prywatny konstruktor domyślny.
     */
    private AuthenticatedUser() {
        this.username = new User().getUsername();
        this.role = new User().getRole();
        this.userId = new User().getUserId();
        this.client = new Client();
    }

    /**
     * Zwraca instancję singletona AuthenticatedUser.
     * @return instancja AuthenticatedUser
     */
    public static AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public Integer getClientId() {
        return client != null ? client.getClientId() : null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}