package com.garaman3.dao;

import com.garaman3.entity.Appointment3;
import com.garaman3.entity.Customer3;
import com.garaman3.entity.TimeSlot3;
import com.garaman3.entity.Vehicle3;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

/**
 * Lop truy xuat du lieu cho Appointment3.
 * Phuc vu Module 1: Book Appointment Online 3.
 */
public class Appointment3DAO {

    private Connection conn;

    public Appointment3DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Kiem tra mot khung gio trong mot ngay cu the con cho hay khong,
     * bang cach dem so lich hen chua huy va so sanh voi suc chua (capacity).
     *
     * @param appointmentDate ngay hen
     * @param timeSlot        khung gio can kiem tra
     * @return true neu con cho, false neu da day
     */
    public boolean isSlotAvailable(LocalDate appointmentDate, TimeSlot3 timeSlot) throws SQLException {
        String sql =
            "SELECT COUNT(*) AS cnt FROM tblAppointment3 " +
            "WHERE timeSlotId = ? AND appointmentDate = ? AND status <> 'Cancelled'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, timeSlot.getId());
            ps.setString(2, appointmentDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int booked = rs.getInt("cnt");
                return booked < timeSlot.getCapacity();
            }
        }
    }

    /**
     * Kiem tra mot xe (theo bien so) da co lich hen con hieu luc (Booked)
     * trong cung mot ngay hay chua.
     *
     * @param licensePlate    bien so xe
     * @param appointmentDate ngay can kiem tra
     * @return true neu xe da co lich Booked trong ngay nay
     */
    public boolean existsBookedOnDate(String licensePlate, LocalDate appointmentDate) throws SQLException {
        String sql =
            "SELECT COUNT(*) AS cnt FROM tblAppointment3 a " +
            "JOIN tblVehicle3 v ON v.id = a.vehicleId " +
            "WHERE v.licensePlate = ? AND a.appointmentDate = ? AND a.status = 'Booked'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            ps.setString(2, appointmentDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("cnt") > 0;
            }
        }
    }

    /**
     * Luu mot lich hen moi voi trang thai "Booked" va sinh ma lich hen.
     *
     * @param appointmentDate ngay hen
     * @param timeSlot        khung gio da chon
     * @param customer        khach hang (da tim hoac vua tao)
     * @param vehicle         xe (da tim hoac vua tao)
     * @param note            ghi chu cua khach hang
     * @return doi tuong Appointment3 vua duoc luu, kem ma lich hen
     */
    public Appointment3 saveAppointment(LocalDate appointmentDate, TimeSlot3 timeSlot,
                                         Customer3 customer, Vehicle3 vehicle, String note) throws SQLException {
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String code = String.format(Locale.ROOT, "LH%05d",
                Math.floorMod(System.currentTimeMillis(), 100000L));

        String sql =
            "INSERT INTO tblAppointment3 " +
            "(id, appointmentCode, appointmentDate, status, note, customerId, vehicleId, timeSlotId) " +
            "VALUES (?, ?, ?, 'Booked', ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, code);
            ps.setString(3, appointmentDate.toString());
            ps.setString(4, note);
            ps.setString(5, customer.getId());
            ps.setString(6, vehicle.getId());
            ps.setString(7, timeSlot.getId());
            ps.executeUpdate();
        }

        Appointment3 appointment = new Appointment3();
        appointment.setId(id);
        appointment.setAppointmentCode(code);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Booked");
        appointment.setNote(note);
        appointment.setCustomer(customer);
        appointment.setVehicle(vehicle);
        appointment.setTimeSlot(timeSlot);
        return appointment;
    }
}
