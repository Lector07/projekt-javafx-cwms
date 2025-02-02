package com.project.cwmsgradle.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double cost;

    @Column(nullable = false)
    private String status;


    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "vehicleId", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Appointment() {
    }

    public Appointment(String description, LocalDate date, double cost, String status, Client client, Vehicle vehicle, User user) {
        this.description = description;
        this.date = date;
        this.cost = cost;
        this.status = status;
        this.client = client;
        this.vehicle = vehicle;
        this.user = user;
    }

    public Appointment(String description, Vehicle selectedVehicle) {
        this.description = description;
        this.vehicle = selectedVehicle;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                ", client=" + client +
                ", vehicle=" + vehicle +
                ", user=" + user +
                '}';
    }
}