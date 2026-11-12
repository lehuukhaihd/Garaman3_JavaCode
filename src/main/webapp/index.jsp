<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GaraMan3 - Trang chủ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="hero">
    <h1>Chào mừng đến GaraMan3</h1>
    <p class="lead">Đặt lịch dịch vụ hoặc xem báo cáo hoạt động của gara.</p>
    <nav class="home-actions" aria-label="Chức năng chính">
        <a class="button" href="${pageContext.request.contextPath}/dat-lich">Đặt lịch hẹn</a>
        <a class="button button-secondary" href="${pageContext.request.contextPath}/dang-nhap">Đăng nhập nhân viên</a>
        <a class="button button-quiet" href="${pageContext.request.contextPath}/bao-cao">Xem báo cáo</a>
    </nav>
</main>
</body>
</html>
