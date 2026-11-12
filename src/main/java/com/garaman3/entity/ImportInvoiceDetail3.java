package com.garaman3.entity;

/**
 * Lop thuc the ImportInvoiceDetail3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class ImportInvoiceDetail3 {

    private String id;
    private int quantity;
    private double unitPrice;
    private double discountRate;
    private double subtotal;
    private String batchNumber;
    private SparePartVariant3 variant;

    public ImportInvoiceDetail3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public SparePartVariant3 getVariant() {
        return variant;
    }

    public void setVariant(SparePartVariant3 variant) {
        this.variant = variant;
    }

}
