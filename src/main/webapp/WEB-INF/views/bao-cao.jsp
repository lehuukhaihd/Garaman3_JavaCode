<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Báo cáo - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <jsp:include page="/WEB-INF/views/fragments/staff-toolbar.jsp"/>
    <a class="back-link" href="${pageContext.request.contextPath}/">← Trang chủ</a>
    <section class="card">
        <h1>Chọn loại báo cáo</h1>
        <p class="muted">Chọn báo cáo cần xem.</p>
        <div class="report-grid">
            <div class="report-card report-disabled">
                <h2>Thống kê dịch vụ</h2>
                <p class="muted">Báo cáo đang được phát triển.</p>
            </div>
            <div class="report-card report-disabled">
                <h2>Thống kê phụ tùng</h2>
                <p class="muted">Báo cáo đang được phát triển.</p>
            </div>
            <div class="report-card report-disabled">
                <h2>Thống kê khách hàng</h2>
                <p class="muted">Báo cáo đang được phát triển.</p>
            </div>
            <a class="report-card" href="${pageContext.request.contextPath}/thong-ke-nha-cung-cap">
                <h2>Thống kê nhà cung cấp</h2>
                <p class="muted">Xem lượng nhập, giá trị và hóa đơn theo khoảng thời gian.</p>
                <span class="report-link">Mở báo cáo →</span>
            </a>
        </div>
    </section>
</main>
</body>
</html>
