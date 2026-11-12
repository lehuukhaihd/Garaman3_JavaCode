package com.garaman3.dao;

import com.garaman3.entity.Customer3;
import com.garaman3.entity.Vehicle3;
import java.sql.*;
import java.util.UUID;

/**
 * Lop truy xuat du lieu cho Vehicle3.
 * Phuc vu Module 1: Book Appointment Online 3.
 */
public class Vehicle3DAO {

    private Connection conn;

    public Vehicle3DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Tim xe theo bien so; neu chua ton tai thi tao moi va gan cho khach hang
     * tuong ung (mot khach hang co the co nhieu xe).
     *
     * @param licensePlate bien so xe
     * @param customer     khach hang so huu xe (dung khi can tao moi)
     * @return doi tuong Vehicle3 da tim duoc hoac vua tao
     */
    public Vehicle3 findOrCreateByPlate(String licensePlate, Customer3 customer) throws SQLException {
        String selectSql = "SELECT id, licensePlate, brand, model, vin FROM tblVehicle3 WHERE licensePlate = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle3 vehicle = new Vehicle3();
                    vehicle.setId(rs.getString("id"));
                    vehicle.setLicensePlate(rs.getString("licensePlate"));
                    vehicle.setBrand(rs.getString("brand"));
                    vehicle.setModel(rs.getString("model"));
                    vehicle.setVin(rs.getString("vin"));
                    vehicle.setCustomer(customer);
                    return vehicle;
                }
            }
        }

        // Chua co xe nay -> tao moi, gan customerId lam khoa ngoai
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String insertSql = "INSERT INTO tblVehicle3 (id, licensePlate, customerId) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, id);
            ps.setString(2, licensePlate);
            ps.setString(3, customer.getId());
            ps.executeUpdate();
        }

        Vehicle3 vehicle = new Vehicle3();
        vehicle.setId(id);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setCustomer(customer);
        return vehicle;
    }
}
