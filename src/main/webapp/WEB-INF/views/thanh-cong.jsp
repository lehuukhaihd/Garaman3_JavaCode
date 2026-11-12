<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đặt lịch thành công - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <section class="card">
        <h1>Đặt lịch thành công!</h1>
        <p class="lead">Mã lịch hẹn của bạn:</p>
        <p class="booking-code"><c:out value="${appointment.appointmentCode}"/></p>
        <dl class="details">
            <dt>Khách hàng</dt>
            <dd><c:out value="${appointment.customer.fullName}"/></dd>
            <dt>Ngày hẹn</dt>
            <dd><c:out value="${appointment.appointmentDate}"/></dd>
            <dt>Khung giờ</dt>
            <dd><c:out value="${appointment.timeSlot.startTime}"/> - <c:out value="${appointment.timeSlot.endTime}"/></dd>
            <dt>Biển số xe</dt>
            <dd><c:out value="${appointment.vehicle.licensePlate}"/></dd>
            <dt>Trạng thái</dt>
            <dd><c:out value="${appointment.status}"/></dd>
        </dl>
        <a class="button" href="${pageContext.request.contextPath}/">Về trang chủ</a>
    </section>
</main>
</body>
</html>
