package com.garaman3.dao;

import com.garaman3.entity.ImportInvoice3;
import com.garaman3.entity.ImportInvoiceDetail3;
import com.garaman3.entity.SparePartVariant3;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lop truy xuat du lieu cho ImportInvoiceDetail3.
 * Phuc vu Module 2: View Supplier Statistics by Import Quantity 3.
 */
public class ImportInvoiceDetail3DAO {

    private Connection conn;

    public ImportInvoiceDetail3DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lay danh sach chi tiet (tung dong bien the phu tung) cua mot hoa don nhap,
     * kem ten bien the phu tung tuong ung (join sang tblSparePartVariant3).
     * Thuoc tinh subtotal la dan xuat, tinh truc tiep trong cau truy van,
     * khong luu trong bang tblImportInvoiceDetail3.
     *
     * @param importInvoice hoa don nhap can xem chi tiet
     * @return danh sach ImportInvoiceDetail3
     */
    public List<ImportInvoiceDetail3> getDetailsByInvoice(ImportInvoice3 importInvoice) throws SQLException {
        String sql =
            "SELECT iid.id, iid.quantity, iid.unitPrice, iid.discountRate, iid.batchNumber, " +
            "  v.id AS variantId, v.sku, v.variantName " +
            "FROM tblImportInvoiceDetail3 iid " +
            "JOIN tblSparePartVariant3 v ON v.id = iid.variantId " +
            "WHERE iid.importInvoiceId = ?";

        List<ImportInvoiceDetail3> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, importInvoice.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportInvoiceDetail3 detail = new ImportInvoiceDetail3();
                    detail.setId(rs.getString("id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getDouble("unitPrice"));
                    detail.setDiscountRate(rs.getDouble("discountRate"));
                    detail.setBatchNumber(rs.getString("batchNumber"));

                    SparePartVariant3 variant = new SparePartVariant3();
                    variant.setId(rs.getString("variantId"));
                    variant.setSku(rs.getString("sku"));
                    variant.setVariantName(rs.getString("variantName"));
                    detail.setVariant(variant);
                    detail.setSubtotal(computeSubtotal(detail));

                    result.add(detail);
                }
            }
        }
        return result;
    }

    /**
     * Thuoc tinh dan xuat subtotal cua mot dong chi tiet, tinh theo cong thuc
     * da chot: subtotal = quantity * unitPrice * (1 - discountRate/100).
     *
     * @param detail dong chi tiet can tinh
     * @return thanh tien sau chiet khau dong
     */
    public double computeSubtotal(ImportInvoiceDetail3 detail) {
        return detail.getQuantity() * detail.getUnitPrice() * (1 - detail.getDiscountRate() / 100.0);
    }
}
