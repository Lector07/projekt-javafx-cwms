package com.project.cwmsgradle.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Klasa reprezentująca klienta.
 */
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String phone;

    @Column
    private String email;

    @OneToMany(mappedBy = "clients", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> clientsVechicle;

    @OneToMany(mappedBy = "appointmentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    /**
     * Konstruktor domyślny.
     */
    public Client() {
    }

    /**
     * Konstruktor z parametrami.
     * @param name imię klienta
     * @param surname nazwisko klienta
     * @param phone numer telefonu klienta
     * @param email adres e-mail klienta
     */
    public Client(String name, String surname, String phone, String email) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Vehicle> getVehicles() {
        return clientsVechicle;
    }

    public void setVehicles(List<Vehicle> clientsVechicle) {
        this.clientsVechicle = clientsVechicle;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return name + " " + surname; // Konkatenacja imienia i nazwiska
    }
}