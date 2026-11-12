-- ===== 16 bang theo dung CSDL da chot (kieu du lieu MySQL thuc) =====
SET NAMES utf8mb4;

CREATE TABLE tblStaff3 (
  id VARCHAR(20) PRIMARY KEY, username VARCHAR(50), password VARCHAR(255),
  fullName NVARCHAR(100), phone VARCHAR(15), email VARCHAR(100), role VARCHAR(20)
);
CREATE TABLE tblTechnician3 (
  id VARCHAR(20) PRIMARY KEY, fullName NVARCHAR(100), phone VARCHAR(15), specialty NVARCHAR(100)
);
CREATE TABLE tblCustomer3 (
  id VARCHAR(20) PRIMARY KEY, fullName NVARCHAR(100), phone VARCHAR(15),
  email VARCHAR(100), address NVARCHAR(255)
);
CREATE TABLE tblVehicle3 (
  id VARCHAR(20) PRIMARY KEY, licensePlate VARCHAR(15), brand NVARCHAR(50),
  model NVARCHAR(50), vin VARCHAR(20),
  customerId VARCHAR(20), FOREIGN KEY (customerId) REFERENCES tblCustomer3(id)
);
CREATE TABLE tblSupplier3 (
  id VARCHAR(20) PRIMARY KEY, name NVARCHAR(150), address NVARCHAR(255),
  phone VARCHAR(15), email VARCHAR(100)
);
CREATE TABLE tblService3 (
  id VARCHAR(20) PRIMARY KEY, name NVARCHAR(150), description NVARCHAR(500),
  price DECIMAL(18,2), warrantyMonths INT
);
CREATE TABLE tblSparePart3 (
  id VARCHAR(20) PRIMARY KEY, name NVARCHAR(150), description NVARCHAR(500)
);
CREATE TABLE tblSparePartVariant3 (
  id VARCHAR(20) PRIMARY KEY, sku VARCHAR(30), variantName NVARCHAR(150),
  brand NVARCHAR(50), unit VARCHAR(20), sellingPrice DECIMAL(18,2),
  stockQuantity INT, minStock INT, warrantyMonths INT,
  sparePartId VARCHAR(20), FOREIGN KEY (sparePartId) REFERENCES tblSparePart3(id)
);
CREATE TABLE tblTimeSlot3 (
  id VARCHAR(20) PRIMARY KEY, startTime TIME, endTime TIME, capacity INT
);
CREATE TABLE tblAppointment3 (
  id VARCHAR(20) PRIMARY KEY, appointmentCode VARCHAR(20), appointmentDate DATE,
  status VARCHAR(20), note NVARCHAR(255),
  customerId VARCHAR(20), vehicleId VARCHAR(20), timeSlotId VARCHAR(20),
  FOREIGN KEY (customerId) REFERENCES tblCustomer3(id),
  FOREIGN KEY (vehicleId) REFERENCES tblVehicle3(id),
  FOREIGN KEY (timeSlotId) REFERENCES tblTimeSlot3(id)
);
CREATE TABLE tblRepairOrder3 (
  id VARCHAR(20) PRIMARY KEY, receivedDate DATETIME, odometer DECIMAL(10,1),
  initialCondition NVARCHAR(500), estimatedTotal DECIMAL(18,2), status VARCHAR(20),
  customerId VARCHAR(20), vehicleId VARCHAR(20), salesStaffId VARCHAR(20), appointmentId VARCHAR(20) NULL,
  FOREIGN KEY (customerId) REFERENCES tblCustomer3(id),
  FOREIGN KEY (vehicleId) REFERENCES tblVehicle3(id),
  FOREIGN KEY (salesStaffId) REFERENCES tblStaff3(id),
  FOREIGN KEY (appointmentId) REFERENCES tblAppointment3(id)
);
CREATE TABLE tblRepairOrderService3 (
  id VARCHAR(20) PRIMARY KEY, unitPrice DECIMAL(18,2), note NVARCHAR(255),
  status VARCHAR(20), warrantyExpiry DATE,
  repairOrderId VARCHAR(20), serviceId VARCHAR(20), technicianId VARCHAR(20),
  FOREIGN KEY (repairOrderId) REFERENCES tblRepairOrder3(id),
  FOREIGN KEY (serviceId) REFERENCES tblService3(id),
  FOREIGN KEY (technicianId) REFERENCES tblTechnician3(id)
);
CREATE TABLE tblRepairOrderPart3 (
  id VARCHAR(20) PRIMARY KEY, quantity INT, unitPrice DECIMAL(18,2),
  status VARCHAR(20), warrantyExpiry DATE,
  repairOrderId VARCHAR(20), variantId VARCHAR(20),
  FOREIGN KEY (repairOrderId) REFERENCES tblRepairOrder3(id),
  FOREIGN KEY (variantId) REFERENCES tblSparePartVariant3(id)
);
CREATE TABLE tblInvoice3 (
  id VARCHAR(20) PRIMARY KEY, paymentDate DATETIME, discount DECIMAL(18,2),
  totalAmount DECIMAL(18,2), paymentMethod VARCHAR(30), paymentStatus VARCHAR(20),
  repairOrderId VARCHAR(20) UNIQUE, salesStaffId VARCHAR(20),
  FOREIGN KEY (repairOrderId) REFERENCES tblRepairOrder3(id),
  FOREIGN KEY (salesStaffId) REFERENCES tblStaff3(id)
);
CREATE TABLE tblImportInvoice3 (
  id VARCHAR(20) PRIMARY KEY, invoiceCode VARCHAR(20), importDate DATE,
  discountAmount DECIMAL(18,2), totalAmount DECIMAL(18,2), status VARCHAR(20),
  supplierId VARCHAR(20), warehouseStaffId VARCHAR(20),
  FOREIGN KEY (supplierId) REFERENCES tblSupplier3(id),
  FOREIGN KEY (warehouseStaffId) REFERENCES tblStaff3(id)
);
CREATE TABLE tblImportInvoiceDetail3 (
  id VARCHAR(20) PRIMARY KEY, quantity INT, unitPrice DECIMAL(18,2),
  discountRate DECIMAL(5,2), batchNumber VARCHAR(30),
  importInvoiceId VARCHAR(20), variantId VARCHAR(20),
  FOREIGN KEY (importInvoiceId) REFERENCES tblImportInvoice3(id),
  FOREIGN KEY (variantId) REFERENCES tblSparePartVariant3(id)
);

-- ===== Du lieu mau dung dung so lieu da chot =====

INSERT INTO tblStaff3 VALUES ('STF01',NULL,NULL,'Tran Van B','0911111111','b@garaman.vn','WarehouseStaff');

INSERT INTO tblTimeSlot3 VALUES
  ('TS01','08:00:00','10:00:00',3),
  ('TS02','10:00:00','12:00:00',2),
  ('TS03','13:00:00','15:00:00',3);

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
