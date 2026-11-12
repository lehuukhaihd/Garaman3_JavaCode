<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thông tin khách hàng - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <a class="back-link" href="${pageContext.request.contextPath}/dat-lich?date=${appointmentDate}">
        ← Đổi ngày hoặc khung giờ
    </a>
    <section class="card">
        <h1>Thông tin khách hàng</h1>
        <p class="muted">Nhập thông tin liên hệ và xe để hoàn tất đặt lịch.</p>

        <div class="appointment-summary">
            <span>Ngày hẹn</span>
            <strong><c:out value="${appointmentDate}"/></strong>
            <span>Khung giờ</span>
            <strong><c:out value="${timeSlot.startTime}"/> – <c:out value="${timeSlot.endTime}"/></strong>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert" role="alert"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/xac-nhan-dat-lich"
              class="booking-form">
            <label for="fullName">Họ và tên <span class="required">*</span></label>
            <input id="fullName" name="fullName" type="text" maxlength="100" required autocomplete="name"
                   value="<c:out value='${param.fullName}'/>">

            <label for="phone">Số điện thoại <span class="required">*</span></label>
            <input id="phone" name="phone" type="tel" maxlength="15" required autocomplete="tel"
                   value="<c:out value='${param.phone}'/>">

            <label for="licensePlate">Biển số xe <span class="required">*</span></label>
            <input id="licensePlate" name="licensePlate" type="text" maxlength="15" required
                   value="<c:out value='${param.licensePlate}'/>">

            <label for="note">Ghi chú <span class="muted">(không bắt buộc)</span></label>
            <textarea id="note" name="note" maxlength="255" rows="3"><c:out value="${param.note}"/></textarea>

            <div class="form-actions">
                <a class="button button-quiet" href="${pageContext.request.contextPath}/dat-lich?date=${appointmentDate}">
                    Quay lại
                </a>
                <button class="button" type="submit">Xác nhận đặt lịch</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
