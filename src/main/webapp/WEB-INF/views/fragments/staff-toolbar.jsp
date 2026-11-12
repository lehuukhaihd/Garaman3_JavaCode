<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="staff-toolbar">
    <span>Đang đăng nhập:
        <strong>
            <c:choose>
                <c:when test="${not empty sessionScope.authenticatedStaff.fullName}">
                    <c:out value="${sessionScope.authenticatedStaff.fullName}"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${sessionScope.authenticatedStaff.username}"/>
                </c:otherwise>
            </c:choose>
        </strong>
        (Quản lý)
    </span>
    <form method="post" action="${pageContext.request.contextPath}/dang-xuat">
        <button class="button button-quiet" type="submit">Đăng xuất</button>
    </form>
</div>
