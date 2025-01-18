package com.project.cwmsgradle.entity;

public class Vehicle {
    private int vehicleId;
    private String registrationNumber;
    private String brandModel;
    private String vehicleType;
    private String productionYear;
    private int clientId;

    public Vehicle(int vehicleId, String registrationNumber, String brandModel, String vehicleType, String productionYear, int clientId) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.brandModel = brandModel;
        this.vehicleType = vehicleType;
        this.productionYear = productionYear;
        this.clientId = clientId;
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

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}