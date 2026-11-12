# GaraMan3 Web

Ứng dụng Java Web Maven dùng Servlet/JSP, MySQL XAMPP và Tomcat 9.

## Cấu hình database

1. Mở XAMPP và bật **MySQL**. Apache của XAMPP có thể bật cùng lúc; nó không thay cho Tomcat.
2. Mở phpMyAdmin tại `http://localhost/phpmyadmin`.
3. Tạo database tên `GaraMan3DB` với collation `utf8mb4_general_ci`.
4. Chọn database vừa tạo, vào **Import**, chọn `src/main/resources/db/schema_mysql.sql`.
5. Mặc định ứng dụng kết nối `localhost:3306`, tài khoản `root`, mật khẩu rỗng (thiết lập thường gặp của XAMPP). Nếu máy bạn dùng thông tin khác, cấu hình các biến môi trường trước khi khởi chạy Tomcat:

   - `GARAMAN3_DB_URL` (mặc định trỏ tới `GaraMan3DB` trên cổng `3306`)
   - `GARAMAN3_DB_USER`
   - `GARAMAN3_DB_PASSWORD`

## Thêm dữ liệu mẫu

Ứng dụng đang đọc/ghi trực tiếp database `GaraMan3DB` trong MySQL XAMPP. Để thêm dữ liệu demo vào database hiện tại, chạy file `src/main/resources/db/seed_demo_data.sql` bằng chức năng **Import** trong phpMyAdmin. File chỉ thêm các ID mẫu riêng bằng `INSERT IGNORE`, không xóa hoặc sửa các bản ghi đang có; có thể chạy lại an toàn.

Dữ liệu ưu tiên Module 2: thêm 6 nhà cung cấp, 176 hóa đơn nhập theo tháng trong năm 2026 và 528 dòng chi tiết phụ tùng; một số hóa đơn có trạng thái chờ để phân biệt với các hóa đơn hoàn tất trong báo cáo. File cũng thêm dữ liệu mẫu cho khách hàng, xe, dịch vụ, kỹ thuật viên, sửa chữa và hóa đơn dịch vụ.

## Đăng nhập nhân viên

Đặt lịch hẹn vẫn công khai. Các trang báo cáo yêu cầu đăng nhập và chỉ tài khoản có vai trò `Manager` được xem thống kê nhà cung cấp. Các trang báo cáo có nút đăng xuất.

Các file SQL không chứa tài khoản/mật khẩu mặc định. Để tạo tài khoản local, thêm nhân viên vào bảng `tblStaff3` trong phpMyAdmin với `username`, `password`, `fullName` và `role` (`Manager`, `WarehouseStaff` hoặc `SalesStaff`). Mật khẩu dạng cũ được chuyển sang PBKDF2 sau lần đăng nhập hợp lệ đầu tiên; không dùng cách tạo tài khoản này cho môi trường công khai.
tài khoản demo: manager01/ B23dcat143

## Build và chạy

1. Cài JDK 11 trở lên, Maven 3.8 trở lên và Tomcat 9.
2. Mở terminal tại thư mục chứa `pom.xml`, chạy `mvn clean package`.
3. Sao chép `target/GaraMan3_Web.war` vào thư mục `webapps` của Tomcat 9 rồi khởi động Tomcat.
4. Mở `http://localhost:8080/GaraMan3_Web/`.

Tomcat 9 dùng cổng mặc định `8080`, Apache XAMPP thường dùng cổng `80`; hai dịch vụ có thể chạy song song. Nếu cổng MySQL không phải `3306`, đặt lại `GARAMAN3_DB_URL`.

## Luồng đặt lịch

1. **Chọn ngày giờ:** chọn ngày từ ngày mai trở đi, xem bảng sức chứa từng khung giờ và chọn khung còn chỗ.
2. **Thông tin khách hàng:** nhập họ tên, số điện thoại, biển số xe và ghi chú tùy chọn.
3. **Hoàn tất:** nhận mã lịch hẹn cùng thông tin ngày giờ và xe.

## Module 2: Thống kê nhà cung cấp

Truy cập **Xem báo cáo** trên trang chủ hoặc mở trực tiếp `http://localhost:8080/GaraMan3_Web/bao-cao`.

Menu báo cáo có đủ bốn loại theo tài liệu: **Thống kê dịch vụ**, **Thống kê phụ tùng**, **Thống kê khách hàng** và **Thống kê nhà cung cấp**. Ba mục đầu hiện được hiển thị trong menu nhưng chưa triển khai trang báo cáo.

1. Chọn **Thống kê nhà cung cấp** và nhập ngày bắt đầu/ngày kết thúc.
2. Xem số lần nhập, tổng số lượng và tổng giá trị sau chiết khấu; nhà cung cấp được sắp xếp theo tổng số lượng giảm dần.
3. Chọn nhà cung cấp để xem hóa đơn nhập đã hoàn tất trong khoảng ngày.
4. Chọn số hóa đơn để xem phụ tùng, số lô, số lượng, đơn giá, chiết khấu và thành tiền.

Mã nhà cung cấp hiển thị theo `id` trong schema (`SUP01`, `SUP02`), vì `tblSupplier3` không có cột mã riêng. Thống kê tính theo chi tiết hóa đơn hiện có; hóa đơn hoàn tất chưa có dòng chi tiết vẫn được tính vào số lần nhập nhưng không cộng số lượng/giá trị chi tiết.

## Cấu trúc chính

```text
src/
├── main/
│   ├── java/com/garaman3/
│   │   ├── controller/   # Servlet
│   │   ├── dao/          # Truy vấn database
│   │   ├── entity/       # Các lớp dữ liệu
│   │   └── util/         # Kết nối MySQL
│   ├── resources/db/     # Schema MySQL/SQLite
│   └── webapp/
│       ├── assets/       # CSS
│       └── WEB-INF/      # JSP và cấu hình web
└── test/java/            # SmokeTest và ModuleTest
```
