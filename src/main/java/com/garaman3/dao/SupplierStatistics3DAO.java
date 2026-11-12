package com.garaman3.dao;

import com.garaman3.entity.SupplierStatistics3;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lop truy xuat du lieu cho SupplierStatistics3.
 * Phuc vu Module 2: View Supplier Statistics by Import Quantity 3.
 */
public class SupplierStatistics3DAO {

    private Connection conn;

    public SupplierStatistics3DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lay thong ke nha cung cap theo luong hang nhap trong mot khoang thoi gian,
     * chi tinh cac hoa don nhap da hoan tat (status = 'Completed'), tong gia tri
     * tinh sau chiet khau, xep giam dan theo tong so luong nhap.
     *
     * @param startDate ngay bat dau
     * @param endDate   ngay ket thuc
     * @return danh sach SupplierStatistics3
     */
    public List<SupplierStatistics3> getSupplierStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql =
            "SELECT s.id, s.name, " +
            "  COUNT(ii.id) AS importCount, " +
            "  SUM(COALESCE(details.totalQuantity, 0)) AS totalQuantity, " +
            "  SUM(CASE WHEN details.importInvoiceId IS NULL THEN 0 " +
            "      ELSE details.detailValue - ii.discountAmount END) AS totalValue " +
            "FROM tblSupplier3 s " +
            "JOIN tblImportInvoice3 ii ON ii.supplierId = s.id " +
            "LEFT JOIN (" +
            "  SELECT importInvoiceId, SUM(quantity) AS totalQuantity, " +
            "    SUM(quantity * unitPrice * (1 - discountRate / 100.0)) AS detailValue " +
            "  FROM tblImportInvoiceDetail3 GROUP BY importInvoiceId" +
            ") details ON details.importInvoiceId = ii.id " +
            "WHERE ii.status = 'Completed' AND ii.importDate BETWEEN ? AND ? " +
            "GROUP BY s.id, s.name " +
            "ORDER BY totalQuantity DESC, s.name";

        List<SupplierStatistics3> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate.toString());
            ps.setString(2, endDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SupplierStatistics3 stat = new SupplierStatistics3();
                    stat.setId(rs.getString("id"));
                    stat.setName(rs.getString("name"));
                    stat.setImportCount(rs.getInt("importCount"));
                    stat.setTotalQuantity(rs.getInt("totalQuantity"));
                    stat.setTotalValue(rs.getDouble("totalValue"));
                    result.add(stat);
                }
            }
        }
        return result;
    }
}
