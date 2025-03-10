package com.project.cwmsgradle.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Klasa reprezentująca pojazd.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicleId;

    @Column(name = "registrationNumber")
    private String registrationNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "productionYear")
    private int productionYear;

    @ManyToOne
    @JoinColumn(name = "clientid", nullable = false)
    private Client clients;

    @OneToMany(mappedBy = "appointmentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    /**
     * Konstruktor domyślny.
     */
    public Vehicle() {
    }

    /**
     * Konstruktor z parametrami.
     * @param registrationNumber numer rejestracyjny pojazdu
     * @param brand marka pojazdu
     * @param model model pojazdu
     * @param productionYear rok produkcji pojazdu
     * @param clients klient powiązany z pojazdem
     */
    public Vehicle(String registrationNumber, String brand, String model, int productionYear, Client clients) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.clients = clients;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public Client getClients() {
        return clients;
    }

    public void setClients(Client clients) {
        this.clients = clients;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return brand + " " + model + " - " + clients.getName() + " " + clients.getSurname();
    }

    public Client getClient() {
        return clients;
    }
}