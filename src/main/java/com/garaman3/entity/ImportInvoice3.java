package com.garaman3.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Lop thuc the ImportInvoice3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class ImportInvoice3 {

    private String id;
    private String invoiceCode;
    private LocalDate importDate;
    private double discountAmount;
    private double totalAmount;
    private String status;
    private Supplier3 supplier;
    private WarehouseStaff3 warehouseStaff;
    private List<ImportInvoiceDetail3> importInvoiceDetailList;

    public ImportInvoice3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Supplier3 getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier3 supplier) {
        this.supplier = supplier;
    }

    public WarehouseStaff3 getWarehouseStaff() {
        return warehouseStaff;
    }

    public void setWarehouseStaff(WarehouseStaff3 warehouseStaff) {
        this.warehouseStaff = warehouseStaff;
    }

    public List<ImportInvoiceDetail3> getImportInvoiceDetailList() {
        return importInvoiceDetailList;
    }

    public void setImportInvoiceDetailList(List<ImportInvoiceDetail3> importInvoiceDetailList) {
        this.importInvoiceDetailList = importInvoiceDetailList;
    }

}
