package com.garaman3.dao;

import com.garaman3.entity.ImportInvoice3;
import com.garaman3.entity.Supplier3;
import com.garaman3.entity.WarehouseStaff3;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lop truy xuat du lieu cho ImportInvoice3.
 * Phuc vu Module 2: View Supplier Statistics by Import Quantity 3.
 */
public class ImportInvoice3DAO {

    private Connection conn;

    public ImportInvoice3DAO(Connection conn) {
        this.conn = conn;
    }

    public Supplier3 getSupplierById(String supplierId) throws SQLException {
        String sql = "SELECT id, name, address, phone, email FROM tblSupplier3 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Supplier3 supplier = new Supplier3();
                supplier.setId(rs.getString("id"));
                supplier.setName(rs.getString("name"));
                supplier.setAddress(rs.getString("address"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                return supplier;
            }
        }
    }

    /**
     * Lay danh sach hoa don nhap da hoan tat cua mot nha cung cap
     * trong mot khoang thoi gian, sap xep theo ngay nhap.
     *
     * @param supplier  nha cung cap can xem
     * @param startDate ngay bat dau
     * @param endDate   ngay ket thuc
     * @return danh sach ImportInvoice3
     */
    public List<ImportInvoice3> getImportsBySupplier(Supplier3 supplier, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql =
            "SELECT ii.id, ii.invoiceCode, ii.importDate, ii.discountAmount, ii.totalAmount, ii.status, " +
            "  ws.id AS staffId, ws.fullName AS staffName " +
            "FROM tblImportInvoice3 ii " +
            "LEFT JOIN tblStaff3 ws ON ws.id = ii.warehouseStaffId " +
            "WHERE ii.supplierId = ? AND ii.status = 'Completed' AND ii.importDate BETWEEN ? AND ? " +
            "ORDER BY ii.importDate, ii.invoiceCode";

        List<ImportInvoice3> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getId());
            ps.setString(2, startDate.toString());
            ps.setString(3, endDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportInvoice3 invoice = new ImportInvoice3();
                    invoice.setId(rs.getString("id"));
                    invoice.setInvoiceCode(rs.getString("invoiceCode"));
                    invoice.setImportDate(java.time.LocalDate.parse(rs.getString("importDate")));
                    invoice.setDiscountAmount(rs.getDouble("discountAmount"));
                    invoice.setTotalAmount(rs.getDouble("totalAmount"));
                    invoice.setStatus(rs.getString("status"));
                    invoice.setSupplier(supplier);
                    if (rs.getString("staffId") != null) {
                        WarehouseStaff3 staff = new WarehouseStaff3();
                        staff.setId(rs.getString("staffId"));
                        staff.setFullName(rs.getString("staffName"));
                        invoice.setWarehouseStaff(staff);
                    }
                    result.add(invoice);
                }
            }
        }
        return result;
    }
}
