<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lỗi - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <section class="card">
        <h1>Chưa thể xử lý yêu cầu</h1>
        <p class="alert" role="alert"><c:out value="${errorMessage}"/></p>
        <a class="button" href="${pageContext.request.contextPath}/">Thử lại sau</a>
    </section>
</main>
</body>
</html>
