package com.garaman3.entity;

import java.time.LocalDate;

/**
 * Lop thuc the Appointment3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class Appointment3 {

    private String id;
    private String appointmentCode;
    private LocalDate appointmentDate;
    private String status;
    private String note;
    private Customer3 customer;
    private Vehicle3 vehicle;
    private TimeSlot3 timeSlot;

    public Appointment3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentCode() {
        return appointmentCode;
    }

    public void setAppointmentCode(String appointmentCode) {
        this.appointmentCode = appointmentCode;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Customer3 getCustomer() {
        return customer;
    }

    public void setCustomer(Customer3 customer) {
        this.customer = customer;
    }

    public Vehicle3 getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle3 vehicle) {
        this.vehicle = vehicle;
    }

    public TimeSlot3 getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot3 timeSlot) {
        this.timeSlot = timeSlot;
    }

}
