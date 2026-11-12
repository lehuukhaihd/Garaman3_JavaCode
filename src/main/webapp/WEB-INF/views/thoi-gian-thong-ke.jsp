<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Khoảng thời gian thống kê - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <jsp:include page="/WEB-INF/views/fragments/staff-toolbar.jsp"/>
    <a class="back-link" href="${pageContext.request.contextPath}/bao-cao">← Chọn loại báo cáo</a>
    <section class="card">
        <h1>Chọn khoảng thời gian</h1>
        <p class="muted">Báo cáo tính trên hóa đơn nhập đã hoàn tất trong khoảng ngày đã chọn.</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert" role="alert"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/thong-ke-nha-cung-cap"
              class="booking-form period-form">
            <label for="startDate">Ngày bắt đầu</label>
            <input id="startDate" name="startDate" type="date" required
                   value="<c:out value='${startDate}'/>">

            <label for="endDate">Ngày kết thúc</label>
            <input id="endDate" name="endDate" type="date" required
                   value="<c:out value='${endDate}'/>">

            <div class="form-actions">
                <a class="button button-quiet" href="${pageContext.request.contextPath}/bao-cao">Quay lại</a>
                <button class="button" type="submit">Xem thống kê</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
