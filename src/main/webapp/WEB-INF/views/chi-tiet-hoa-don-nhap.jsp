<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi tiết hóa đơn nhập - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page page-wide">
    <jsp:include page="/WEB-INF/views/fragments/staff-toolbar.jsp"/>
    <c:url var="backUrl" value="/bao-cao/nha-cung-cap/nhap-hang">
        <c:param name="supplierId" value="${supplier.id}"/>
        <c:param name="startDate" value="${startDate}"/>
        <c:param name="endDate" value="${endDate}"/>
    </c:url>
    <a class="back-link" href="${backUrl}">← Danh sách lần nhập</a>
    <section class="card">
        <h1><c:out value="${invoice.invoiceCode}"/></h1>
        <div class="invoice-summary">
            <div><span>Nhà cung cấp</span><strong><c:out value="${supplier.name}"/></strong></div>
            <div><span>Ngày nhập</span><strong><c:out value="${invoice.importDate}"/></strong></div>
            <div><span>Nhân viên kho</span>
                <strong>
                    <c:choose>
                        <c:when test="${not empty invoice.warehouseStaff}">
                            <c:out value="${invoice.warehouseStaff.fullName}"/>
                        </c:when>
                        <c:otherwise>Chưa cập nhật</c:otherwise>
                    </c:choose>
                </strong>
            </div>
            <div><span>Trạng thái</span><strong><c:out value="${invoice.status}"/></strong></div>
        </div>

        <c:choose>
            <c:when test="${empty details}">
                <div class="empty-state">Hóa đơn này chưa có dòng chi tiết phụ tùng.</div>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>TT</th>
                            <th>Mã phụ tùng</th>
                            <th>Tên phụ tùng</th>
                            <th>Số lô</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Chiết khấu</th>
                            <th>Thành tiền</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="detail" items="${details}" varStatus="status">
                            <tr>
                                <td><c:out value="${status.count}"/></td>
                                <td><c:out value="${detail.variant.sku}"/></td>
                                <td><c:out value="${detail.variant.variantName}"/></td>
                                <td><c:out value="${detail.batchNumber}"/></td>
                                <td><fmt:formatNumber value="${detail.quantity}" pattern="#,##0"/></td>
                                <td><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0"/> ₫</td>
                                <td><fmt:formatNumber value="${detail.discountRate}" pattern="#,##0.##"/>%</td>
                                <td class="amount"><fmt:formatNumber value="${detail.subtotal}" pattern="#,##0"/> ₫</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="7">Tổng tiền hóa đơn</th>
                            <th class="amount"><fmt:formatNumber value="${invoice.totalAmount}" pattern="#,##0"/> ₫</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
