-- ===== 16 bang theo dung CSDL da chot (chuyen kieu cho tuong thich SQLite) =====

CREATE TABLE tblStaff3 (
  id TEXT PRIMARY KEY, username TEXT, password TEXT,
  fullName TEXT, phone TEXT, email TEXT, role TEXT
);
CREATE TABLE tblTechnician3 (
  id TEXT PRIMARY KEY, fullName TEXT, phone TEXT, specialty TEXT
);
CREATE TABLE tblCustomer3 (
  id TEXT PRIMARY KEY, fullName TEXT, phone TEXT, email TEXT, address TEXT
);
CREATE TABLE tblVehicle3 (
  id TEXT PRIMARY KEY, licensePlate TEXT, brand TEXT, model TEXT, vin TEXT,
  customerId TEXT REFERENCES tblCustomer3(id)
);
CREATE TABLE tblSupplier3 (
  id TEXT PRIMARY KEY, name TEXT, address TEXT, phone TEXT, email TEXT
);
CREATE TABLE tblService3 (
  id TEXT PRIMARY KEY, name TEXT, description TEXT, price REAL, warrantyMonths INTEGER
);
CREATE TABLE tblSparePart3 (
  id TEXT PRIMARY KEY, name TEXT, description TEXT
);
CREATE TABLE tblSparePartVariant3 (
  id TEXT PRIMARY KEY, sku TEXT, variantName TEXT, brand TEXT, unit TEXT,
  sellingPrice REAL, stockQuantity INTEGER, minStock INTEGER, warrantyMonths INTEGER,
  sparePartId TEXT REFERENCES tblSparePart3(id)
);
CREATE TABLE tblTimeSlot3 (
  id TEXT PRIMARY KEY, startTime TEXT, endTime TEXT, capacity INTEGER
);
CREATE TABLE tblAppointment3 (
  id TEXT PRIMARY KEY, appointmentCode TEXT, appointmentDate TEXT, status TEXT, note TEXT,
  customerId TEXT REFERENCES tblCustomer3(id),
  vehicleId TEXT REFERENCES tblVehicle3(id),
  timeSlotId TEXT REFERENCES tblTimeSlot3(id)
);
CREATE TABLE tblRepairOrder3 (
  id TEXT PRIMARY KEY, receivedDate TEXT, odometer REAL, initialCondition TEXT,
  estimatedTotal REAL, status TEXT,
  customerId TEXT, vehicleId TEXT, salesStaffId TEXT, appointmentId TEXT
);
CREATE TABLE tblRepairOrderService3 (
  id TEXT PRIMARY KEY, unitPrice REAL, note TEXT, status TEXT, warrantyExpiry TEXT,
  repairOrderId TEXT, serviceId TEXT, technicianId TEXT
);
CREATE TABLE tblRepairOrderPart3 (
  id TEXT PRIMARY KEY, quantity INTEGER, unitPrice REAL, status TEXT, warrantyExpiry TEXT,
  repairOrderId TEXT, variantId TEXT
);
CREATE TABLE tblInvoice3 (
  id TEXT PRIMARY KEY, paymentDate TEXT, discount REAL, totalAmount REAL,
  paymentMethod TEXT, paymentStatus TEXT, repairOrderId TEXT UNIQUE, salesStaffId TEXT
);
CREATE TABLE tblImportInvoice3 (
  id TEXT PRIMARY KEY, invoiceCode TEXT, importDate TEXT, discountAmount REAL,
  totalAmount REAL, status TEXT,
  supplierId TEXT REFERENCES tblSupplier3(id),
  warehouseStaffId TEXT REFERENCES tblStaff3(id)
);
CREATE TABLE tblImportInvoiceDetail3 (
  id TEXT PRIMARY KEY, quantity INTEGER, unitPrice REAL, discountRate REAL, batchNumber TEXT,
  importInvoiceId TEXT REFERENCES tblImportInvoice3(id),
  variantId TEXT REFERENCES tblSparePartVariant3(id)
);

-- ===== Du lieu mau dung dung so lieu da chot =====

INSERT INTO tblStaff3 VALUES ('STF01',NULL,NULL,'Tran Van B','0911111111','b@garaman.vn','WarehouseStaff');

INSERT INTO tblTimeSlot3 VALUES
  ('TS01','08:00:00','10:00:00',3),
  ('TS02','10:00:00','12:00:00',2),
  ('TS03','13:00:00','15:00:00',3);

-- TS02 da day: 2 lich Booked trong ngay 2026-09-28 (bang capacity)
INSERT INTO tblCustomer3 VALUES ('CUS01','Le Thi C','0909090909',NULL,NULL);
INSERT INTO tblVehicle3 VALUES ('VEH01','29A-11111',NULL,NULL,NULL,'CUS01');
INSERT INTO tblVehicle3 VALUES ('VEH02','29A-22222',NULL,NULL,NULL,'CUS01');
INSERT INTO tblAppointment3 VALUES ('APT01','LH0001','2026-09-28','Booked',NULL,'CUS01','VEH01','TS02');
INSERT INTO tblAppointment3 VALUES ('APT02','LH0002','2026-09-28','Booked',NULL,'CUS01','VEH02','TS02');

INSERT INTO tblSupplier3 VALUES ('SUP01','Cty Phu tung Thanh Cong',NULL,NULL,NULL);
INSERT INTO tblSupplier3 VALUES ('SUP02','Cty TNHH Phu tung Viet Duc',NULL,NULL,NULL);

INSERT INTO tblSparePart3 VALUES ('SP01','Loc dau',NULL);
INSERT INTO tblSparePart3 VALUES ('SP02','Bugi',NULL);
INSERT INTO tblSparePartVariant3 VALUES ('VAR01','PT001','Loc dau - Toyota Vios',NULL,NULL,120000,100,10,6,'SP01');
INSERT INTO tblSparePartVariant3 VALUES ('VAR02','PT002','Bugi - NGK Iridium',NULL,NULL,90000,200,20,12,'SP02');

INSERT INTO tblImportInvoice3 VALUES ('IMP01','HDN0089','2026-08-05',0,14100000,'Completed','SUP01','STF01');
INSERT INTO tblImportInvoice3 VALUES ('IMP02','HDN0102','2026-08-14',0,30900000,'Completed','SUP01','STF01');

INSERT INTO tblImportInvoiceDetail3 VALUES ('D01',50,120000,0,'LOT-0801','IMP01','VAR01');
INSERT INTO tblImportInvoiceDetail3 VALUES ('D02',100,90000,10,'LOT-0802','IMP01','VAR02');
