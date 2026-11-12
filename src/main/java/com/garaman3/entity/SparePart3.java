package com.garaman3.entity;

import java.util.List;

/**
 * Lop thuc the SparePart3.
 * Sinh tu bieu do lop thiet ke - khong chua phuong thuc nghiep vu (da chuyen sang DAO).
 */
public class SparePart3 {

    private String id;
    private String name;
    private String description;
    private List<SparePartVariant3> variantList;

    public SparePart3() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SparePartVariant3> getVariantList() {
        return variantList;
    }

    public void setVariantList(List<SparePartVariant3> variantList) {
        this.variantList = variantList;
    }

}
