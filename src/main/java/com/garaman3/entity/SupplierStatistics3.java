package com.garaman3.entity;

/**
 * Lop thuc the SupplierStatistics3 (ke thua Supplier3).
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class SupplierStatistics3 extends Supplier3 {

    private String id;
    private String name;
    private int importCount;
    private int totalQuantity;
    private double totalValue;

    public SupplierStatistics3() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImportCount() {
        return importCount;
    }

    public void setImportCount(int importCount) {
        this.importCount = importCount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

}
