<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lần nhập hàng - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page page-wide">
    <jsp:include page="/WEB-INF/views/fragments/staff-toolbar.jsp"/>
    <c:url var="backUrl" value="/bao-cao/nha-cung-cap">
        <c:param name="startDate" value="${startDate}"/>
        <c:param name="endDate" value="${endDate}"/>
    </c:url>
    <a class="back-link" href="${backUrl}">← Quay lại thống kê</a>
    <section class="card">
        <h1><c:out value="${supplier.name}"/></h1>
        <p class="muted">
            Mã NCC: <strong><c:out value="${supplier.id}"/></strong>
            · Từ <c:out value="${startDate}"/> đến <c:out value="${endDate}"/>
        </p>

        <c:choose>
            <c:when test="${empty invoices}">
                <div class="empty-state">Nhà cung cấp không có hóa đơn nhập hoàn tất trong khoảng ngày này.</div>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>TT</th>
                            <th>Số hóa đơn</th>
                            <th>Ngày nhập</th>
                            <th>Nhân viên kho</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="invoice" items="${invoices}" varStatus="status">
                            <c:url var="invoiceUrl" value="/bao-cao/hoa-don-nhap">
                                <c:param name="invoiceId" value="${invoice.id}"/>
                                <c:param name="supplierId" value="${supplier.id}"/>
                                <c:param name="startDate" value="${startDate}"/>
                                <c:param name="endDate" value="${endDate}"/>
                            </c:url>
                            <tr class="clickable-row" data-href="<c:out value='${invoiceUrl}'/>">
                                <td><c:out value="${status.count}"/></td>
                                <td><a class="table-link" href="${invoiceUrl}"><c:out value="${invoice.invoiceCode}"/></a></td>
                                <td><c:out value="${invoice.importDate}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty invoice.warehouseStaff}">
                                            <c:out value="${invoice.warehouseStaff.fullName}"/>
                                        </c:when>
                                        <c:otherwise>Chưa cập nhật</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="amount"><fmt:formatNumber value="${invoice.totalAmount}" pattern="#,##0"/> ₫</td>
                                <td><span class="availability available"><c:out value="${invoice.status}"/></span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/table-rows.js?v=20261112-1444"></script>
</body>
</html>
