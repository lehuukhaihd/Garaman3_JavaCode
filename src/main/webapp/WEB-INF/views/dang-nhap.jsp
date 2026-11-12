<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập nhân viên - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <a class="back-link" href="${pageContext.request.contextPath}/">← Trang chủ</a>
    <section class="card">
        <h1>Đăng nhập nhân viên</h1>
        <p class="muted">Đăng nhập bằng tài khoản quản lý để xem báo cáo.</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert" role="alert"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/dang-nhap"
              class="booking-form login-form">
            <label for="username">Tên đăng nhập</label>
            <input id="username" name="username" type="text" autocomplete="username"
                   maxlength="50" required>

            <label for="password">Mật khẩu</label>
            <input id="password" name="password" type="password" autocomplete="current-password"
                   maxlength="255" required>

            <div class="form-actions">
                <a class="button button-quiet" href="${pageContext.request.contextPath}/dat-lich">Đặt lịch công khai</a>
                <button class="button" type="submit">Đăng nhập</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
