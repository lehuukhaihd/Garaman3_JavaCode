package com.garaman3.entity;

/**
 * Lop thuc the Vehicle3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class Vehicle3 {

    private String id;
    private String licensePlate;
    private String brand;
    private String model;
    private String vin;
    private Customer3 customer;

    public Vehicle3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Customer3 getCustomer() {
        return customer;
    }

    public void setCustomer(Customer3 customer) {
        this.customer = customer;
    }

}
