package com.garaman3;

import com.garaman3.dao.*;
import com.garaman3.entity.*;
import com.garaman3.util.DBContext;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Chay that toan bo 7 lop DAO qua CSDL MySQL (schema_mysql.sql),
 * dung dung so lieu mau da chot, de kiem chung logic + cau SQL chay duoc.
 */
public class ModuleTest {

    static int pass = 0, fail = 0;

    public static void main(String[] args) throws Exception {
        Connection conn = DBContext.getConnection();
        // Schema va du lieu mau da duoc nap san tu schema_mysql.sql truoc do

        System.out.println("===== MODULE 1: Book Appointment Online 3 =====");
        testModule1(conn);

        System.out.println();
        System.out.println("===== MODULE 2: View Supplier Statistics by Import Quantity 3 =====");
        testModule2(conn);

        System.out.println();
        System.out.println("KET QUA: " + pass + " PASS, " + fail + " FAIL");
        conn.close();
        if (fail > 0) System.exit(1);
    }

    static void testModule1(Connection conn) throws SQLException {
        TimeSlot3DAO timeSlotDAO = new TimeSlot3DAO(conn);
        Appointment3DAO appointmentDAO = new Appointment3DAO(conn);
        Customer3DAO customerDAO = new Customer3DAO(conn);
        Vehicle3DAO vehicleDAO = new Vehicle3DAO(conn);

        LocalDate day = LocalDate.of(2026, 9, 28);

        // 1. getSlotsByDate - ky vong TS01 con 3, TS02 da day (0 con lai), TS03 con 3
        List<TimeSlot3> slots = timeSlotDAO.getSlotsByDate(day);
        check("getSlotsByDate() tra ve 3 khung gio", slots.size() == 3);
        for (TimeSlot3 s : slots) {
            System.out.println("  - " + s.getId() + ": " + s.getStartTime() + "-" + s.getEndTime()
                    + " (capacity=" + s.getCapacity() + ")");
        }

        // 2. isSlotAvailable - TS01 con cho (0 lich dat / capacity 3)
        TimeSlot3 ts01 = slots.stream().filter(s -> s.getId().equals("TS01")).findFirst().get();
        boolean available1 = appointmentDAO.isSlotAvailable(day, ts01);
        check("isSlotAvailable(TS01) = true (con cho)", available1);

        // 3. isSlotAvailable - TS02 da day (2 lich Booked / capacity 2)
        TimeSlot3 ts02 = slots.stream().filter(s -> s.getId().equals("TS02")).findFirst().get();
        boolean available2 = appointmentDAO.isSlotAvailable(day, ts02);
        check("isSlotAvailable(TS02) = false (da day)", !available2);

        // 4. existsBookedOnDate - xe 29A-11111 da co lich Booked trong ngay
        boolean exists1 = appointmentDAO.existsBookedOnDate("29A-11111", day);
        check("existsBookedOnDate(29A-11111) = true", exists1);

        // 5. existsBookedOnDate - bien so moi 30A-99999 chua co lich
        boolean exists2 = appointmentDAO.existsBookedOnDate("30A-99999", day);
        check("existsBookedOnDate(30A-99999) = false (xe moi)", !exists2);

        // 6. findOrCreateByPhone - tao khach hang moi (dung du lieu mau da chot)
        Customer3 customer = customerDAO.findOrCreateByPhone("Nguyen Van A", "0987654321");
        check("findOrCreateByPhone() tao Customer3 moi, co id", customer.getId() != null);
        System.out.println("  -> Customer3 moi: id=" + customer.getId() + " fullName=" + customer.getFullName());

        // 7. goi lai lan 2 voi cung SDT -> phai tra ve CUNG 1 id (khong tao trung)
        Customer3 customerAgain = customerDAO.findOrCreateByPhone("Nguyen Van A", "0987654321");
        check("findOrCreateByPhone() goi lan 2 tra ve CUNG id (khong trung)",
                customer.getId().equals(customerAgain.getId()));

        // 8. findOrCreateByPlate - tao xe moi gan voi khach vua tao
        Vehicle3 vehicle = vehicleDAO.findOrCreateByPlate("30A-99999", customer);
        check("findOrCreateByPlate() tao Vehicle3 moi, co id", vehicle.getId() != null);

        // 9. saveAppointment - luu lich hen moi, dung du lieu mau da chot
        Appointment3 appt = appointmentDAO.saveAppointment(day, ts01, customer, vehicle, "Bao duong dinh ky");
        check("saveAppointment() sinh ma lich hen bat dau bang LH", appt.getAppointmentCode().startsWith("LH"));
        check("saveAppointment() trang thai = Booked", appt.getStatus().equals("Booked"));
        System.out.println("  -> Appointment3 moi: code=" + appt.getAppointmentCode());

        // 10. kiem tra lai TS01 sau khi them 1 lich -> van con cho (1/3)
        boolean stillAvailable = appointmentDAO.isSlotAvailable(day, ts01);
        check("Sau khi dat, TS01 van con cho (1/3)", stillAvailable);
    }

    static void testModule2(Connection conn) throws SQLException {
        SupplierStatistics3DAO statDAO = new SupplierStatistics3DAO(conn);
        ImportInvoice3DAO invoiceDAO = new ImportInvoice3DAO(conn);
        ImportInvoiceDetail3DAO detailDAO = new ImportInvoiceDetail3DAO(conn);

        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 31);

        // 1. getSupplierStatistics - ky vong SUP01 co importCount=2, totalQuantity=150, totalValue=14.100.000+... tu 2 HD
        //    (chi co du lieu chi tiet cho HDN0089 trong du lieu mau, HDN0102 chua co dong chi tiet)
        List<SupplierStatistics3> stats = statDAO.getSupplierStatistics(start, end);
        check("getSupplierStatistics() tra ve it nhat 1 NCC", stats.size() >= 1);
        for (SupplierStatistics3 s : stats) {
            System.out.println("  - " + s.getId() + " " + s.getName()
                    + ": importCount=" + s.getImportCount()
                    + " totalQuantity=" + s.getTotalQuantity()
                    + " totalValue=" + s.getTotalValue());
        }
        SupplierStatistics3 sup01Stat = stats.stream().filter(s -> s.getId().equals("SUP01")).findFirst().orElse(null);
        check("SUP01 co trong ket qua thong ke", sup01Stat != null);
        check("SUP01 totalQuantity = 150 (50 + 100 tu HDN0089)", sup01Stat.getTotalQuantity() == 150);
        check("SUP01 totalValue = 14100000.0 (dung so lieu da chot)", sup01Stat.getTotalValue() == 14100000.0);

        // 2. getImportsBySupplier - lay 2 hoa don cua SUP01
        Supplier3 sup01 = new Supplier3();
        sup01.setId("SUP01");
        List<ImportInvoice3> invoices = invoiceDAO.getImportsBySupplier(sup01, start, end);
        check("getImportsBySupplier() tra ve 2 hoa don", invoices.size() == 2);
        for (ImportInvoice3 inv : invoices) {
            System.out.println("  - " + inv.getInvoiceCode() + ": " + inv.getTotalAmount());
        }
        ImportInvoice3 hdn0089 = invoices.stream().filter(i -> i.getInvoiceCode().equals("HDN0089")).findFirst().get();
        check("HDN0089 co totalAmount = 14100000.0", hdn0089.getTotalAmount() == 14100000.0);

        // 3. getDetailsByInvoice - lay chi tiet HDN0089, ky vong 2 dong (PT001, PT002)
        List<ImportInvoiceDetail3> details = detailDAO.getDetailsByInvoice(hdn0089);
        check("getDetailsByInvoice(HDN0089) tra ve 2 dong", details.size() == 2);
        double sumSubtotal = 0;
        for (ImportInvoiceDetail3 d : details) {
            double sub = detailDAO.computeSubtotal(d);
            sumSubtotal += sub;
            System.out.println("  - " + d.getVariant().getSku() + " (" + d.getVariant().getVariantName()
                    + "), lo=" + d.getBatchNumber() + ", SL=" + d.getQuantity()
                    + ", CK=" + d.getDiscountRate() + "%, thanh tien=" + sub);
        }
        check("Tong 2 dong chi tiet = 14100000.0 (6.000.000 + 8.100.000)", sumSubtotal == 14100000.0);
    }

    static void check(String label, boolean ok) {
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + label);
        if (ok) pass++; else fail++;
    }
}
