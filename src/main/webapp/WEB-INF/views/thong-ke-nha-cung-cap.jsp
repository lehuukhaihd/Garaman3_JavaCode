<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thống kê nhà cung cấp - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page page-wide">
    <jsp:include page="/WEB-INF/views/fragments/staff-toolbar.jsp"/>
    <a class="back-link" href="${pageContext.request.contextPath}/thong-ke-nha-cung-cap">← Đổi khoảng thời gian</a>
    <section class="card">
        <h1>Thống kê nhà cung cấp</h1>
        <p class="muted">
            Từ <strong><c:out value="${startDate}"/></strong>
            đến <strong><c:out value="${endDate}"/></strong>
            · Nhấn vào nhà cung cấp để xem các lần nhập.
        </p>

        <c:choose>
            <c:when test="${empty statistics}">
                <div class="empty-state">Không có dữ liệu nhà cung cấp trong khoảng thời gian này.</div>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>TT</th>
                            <th>Mã NCC</th>
                            <th>Tên nhà cung cấp</th>
                            <th>Số lần nhập</th>
                            <th>Tổng SL</th>
                            <th>Tổng giá trị</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="supplier" items="${statistics}" varStatus="status">
                            <c:url var="supplierUrl" value="/bao-cao/nha-cung-cap/nhap-hang">
                                <c:param name="supplierId" value="${supplier.id}"/>
                                <c:param name="startDate" value="${startDate}"/>
                                <c:param name="endDate" value="${endDate}"/>
                            </c:url>
                            <tr class="clickable-row" data-href="<c:out value='${supplierUrl}'/>">
                                <td><c:out value="${status.count}"/></td>
                                <td><c:out value="${supplier.id}"/></td>
                                <td>
                                    <a class="table-link" href="${supplierUrl}">
                                        <c:out value="${supplier.name}"/>
                                    </a>
                                </td>
                                <td><fmt:formatNumber value="${supplier.importCount}" pattern="#,##0"/></td>
                                <td><fmt:formatNumber value="${supplier.totalQuantity}" pattern="#,##0"/></td>
                                <td class="amount">
                                    <fmt:formatNumber value="${supplier.totalValue}" pattern="#,##0"/> ₫
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="form-actions">
            <a class="button button-quiet" href="${pageContext.request.contextPath}/bao-cao">Danh sách báo cáo</a>
            <a class="button" href="${pageContext.request.contextPath}/thong-ke-nha-cung-cap">Chọn ngày khác</a>
        </div>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/table-rows.js?v=20261112-1444"></script>
</body>
</html>
