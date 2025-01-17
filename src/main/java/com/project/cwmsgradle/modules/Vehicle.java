package com.project.cwmsgradle.modules;

public class Vehicle {
    private String registrationNumber;
    private String brandModel;
    private String vehicleType;
    private String productionYear;

    public Vehicle(String registrationNumber, String brandModel, String vehicleType, String productionYear) {
        this.registrationNumber = registrationNumber;
        this.brandModel = brandModel;
        this.vehicleType = vehicleType;
        this.productionYear = productionYear;
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
}