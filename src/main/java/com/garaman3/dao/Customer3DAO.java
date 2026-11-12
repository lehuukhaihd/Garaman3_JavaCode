package com.garaman3.dao;

import com.garaman3.entity.Customer3;
import java.sql.*;
import java.util.UUID;

/**
 * Lop truy xuat du lieu cho Customer3.
 * Phuc vu Module 1: Book Appointment Online 3.
 */
public class Customer3DAO {

    private Connection conn;

    public Customer3DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Tim khach hang theo so dien thoai; neu chua ton tai thi tao moi.
     * Day la diem luu thong tin khach hang moi khi dat lich lan dau.
     *
     * @param fullName ho ten khach hang
     * @param phone    so dien thoai (dung de dinh danh khach hang)
     * @return doi tuong Customer3 da tim duoc hoac vua tao
     */
    public Customer3 findOrCreateByPhone(String fullName, String phone) throws SQLException {
        String selectSql = "SELECT id, fullName, phone, email, address FROM tblCustomer3 WHERE phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer3 customer = new Customer3();
                    customer.setId(rs.getString("id"));
                    customer.setFullName(rs.getString("fullName"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAddress(rs.getString("address"));
                    return customer;
                }
            }
        }

        // Chua co khach hang nay -> tao moi
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String insertSql = "INSERT INTO tblCustomer3 (id, fullName, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, id);
            ps.setString(2, fullName);
            ps.setString(3, phone);
            ps.executeUpdate();
        }

        Customer3 customer = new Customer3();
        customer.setId(id);
        customer.setFullName(fullName);
        customer.setPhone(phone);
        return customer;
    }
}
