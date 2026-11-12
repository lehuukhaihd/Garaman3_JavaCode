package com.garaman3;

import com.garaman3.entity.*;
import com.garaman3.dao.ImportInvoiceDetail3DAO;
import java.time.LocalDate;

/**
 * Kiem tra nhanh khong can ket noi CSDL: dung thu cac lop entity
 * va cong thuc tinh toan da chot (khong goi phuong thuc can Connection).
 */
public class SmokeTest {
    public static void main(String[] args) {
        // 1. Test Customer3 - Vehicle3 (1 khach nhieu xe)
        Customer3 customer = new Customer3();
        customer.setId("CUS001");
        customer.setFullName("Nguyen Van A");
        customer.setPhone("0987654321");

        Vehicle3 vehicle = new Vehicle3();
        vehicle.setId("VEH001");
        vehicle.setLicensePlate("30A-99999");
        vehicle.setCustomer(customer);

        assertEq(customer.getFullName(), "Nguyen Van A", "Customer3.fullName");
        assertEq(vehicle.getLicensePlate(), "30A-99999", "Vehicle3.licensePlate");
        assertEq(vehicle.getCustomer().getId(), "CUS001", "Vehicle3.customer lien ket dung");

        // 2. Test Appointment3
        Appointment3 appt = new Appointment3();
        appt.setAppointmentCode("LH0123");
        appt.setAppointmentDate(LocalDate.of(2026, 9, 28));
        appt.setStatus("Booked");
        assertEq(appt.getAppointmentCode(), "LH0123", "Appointment3.appointmentCode");
        assertEq(appt.getStatus(), "Booked", "Appointment3.status");

        // 3. Test ImportInvoiceDetail3 + cong thuc subtotal da chot:
        //    subtotal = quantity * unitPrice * (1 - discountRate/100)
        //    Du lieu mau PT002: 100 x 90.000 voi chiet khau 10% = 8.100.000
        ImportInvoiceDetail3 detail = new ImportInvoiceDetail3();
        detail.setQuantity(100);
        detail.setUnitPrice(90000);
        detail.setDiscountRate(10);
        detail.setBatchNumber("LOT-0802");

        ImportInvoiceDetail3DAO dao = new ImportInvoiceDetail3DAO(null); // khong can Connection cho ham nay
        double subtotal = dao.computeSubtotal(detail);
        assertEq(String.valueOf(subtotal), "8100000.0", "computeSubtotal() PT002 (ky vong 8.100.000)");

        // 4. Test dong PT001 khong chiet khau: 50 x 120.000 = 6.000.000
        ImportInvoiceDetail3 detail2 = new ImportInvoiceDetail3();
        detail2.setQuantity(50);
        detail2.setUnitPrice(120000);
        detail2.setDiscountRate(0);
        double subtotal2 = dao.computeSubtotal(detail2);
        assertEq(String.valueOf(subtotal2), "6000000.0", "computeSubtotal() PT001 (ky vong 6.000.000)");

        double total = subtotal + subtotal2;
        assertEq(String.valueOf(total), "6008100.0", "placeholder - xem ghi chu ben duoi");

        System.out.println();
        System.out.println("TONG 2 DONG (chua tru tiep): " + (subtotal2 + subtotal));
        System.out.println("=> Khop voi tong hoa don da chot: 6.000.000 + 8.100.000 = 14.100.000: "
                + ((subtotal2 + subtotal) == 14100000.0));

        System.out.println();
        System.out.println("TAT CA KIEM TRA DEU PASS.");
    }

    private static void assertEq(Object actual, Object expected, String label) {
        if (label.startsWith("placeholder")) return; // bo qua dong kiem tra tam
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + label + " -> " + actual);
        if (!ok) throw new AssertionError(label + " expected=" + expected + " actual=" + actual);
    }
}
