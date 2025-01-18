package com.project.cwmsgradle.entity;

public class Client {
    private int clientId;
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String mail;

    public Client(int clientId, int id, String name, String surname, String phone, String mail) {
        this.clientId = clientId;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
    }

    public int getClientId() {
        return clientId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }
}