package com.garaman3.entity;

import java.util.List;

/**
 * Lop thuc the Customer3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class Customer3 {

    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private List<Vehicle3> vehicleList;

    public Customer3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Vehicle3> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle3> vehicleList) {
        this.vehicleList = vehicleList;
    }

}
