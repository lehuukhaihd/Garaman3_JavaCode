<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Không có quyền truy cập - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <section class="card">
        <h1>Không có quyền truy cập</h1>
        <p class="alert" role="alert">Chỉ nhân viên có vai trò Quản lý mới xem được báo cáo này.</p>
        <div class="home-actions">
            <form method="post" action="${pageContext.request.contextPath}/dang-xuat">
                <button class="button" type="submit">Đăng xuất</button>
            </form>
            <a class="button button-quiet" href="${pageContext.request.contextPath}/">Về trang chủ</a>
        </div>
    </section>
</main>
</body>
</html>
