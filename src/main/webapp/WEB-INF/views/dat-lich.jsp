<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chọn ngày và giờ - GaraMan3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css?v=20261112-1718">
</head>
<body>
<main class="page">
    <a class="back-link" href="${pageContext.request.contextPath}/">← Trang chủ</a>
    <section class="card">
        <h1>Chọn ngày và khung giờ</h1>
        <p class="muted">Lịch hẹn được nhận từ ngày mai. Chọn ngày để xem số chỗ còn lại.</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert" role="alert"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/dat-lich" class="date-picker">
            <label for="date">Ngày hẹn</label>
            <div class="date-picker-controls">
                <input id="date" name="date" type="date" required min="${minimumDate}"
                       value="${appointmentDate}">
                <button class="button button-secondary" type="submit">Xem khung giờ</button>
            </div>
        </form>

        <c:set var="hasAvailableSlot" value="false"/>
        <c:forEach var="slot" items="${timeSlots}">
            <c:if test="${slot.remainingCapacity gt 0}">
                <c:set var="hasAvailableSlot" value="true"/>
            </c:if>
        </c:forEach>

        <form method="post" action="${pageContext.request.contextPath}/thong-tin-khach"
              id="slot-form" class="slot-form">
            <input type="hidden" name="date" value="${appointmentDate}">
            <div class="table-wrap">
                <table class="slot-table">
                    <thead>
                    <tr>
                        <th>Chọn</th>
                        <th>STT</th>
                        <th>Khung giờ</th>
                        <th>Số chỗ còn lại</th>
                        <th>Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="slot" items="${timeSlots}" varStatus="status">
                        <tr class="slot-row ${slot.remainingCapacity eq 0 ? 'slot-full' : ''}"
                            data-remaining="${slot.remainingCapacity}">
                            <td>
                                <input type="radio" name="timeSlotId" value="${slot.id}"
                                       aria-label="Chọn khung giờ ${slot.startTime} đến ${slot.endTime}"
                                       <c:if test="${slot.remainingCapacity eq 0}">disabled</c:if>
                                       <c:if test="${slot.id eq selectedSlotId and slot.remainingCapacity gt 0}">checked</c:if>>
                            </td>
                            <td><c:out value="${status.count}"/></td>
                            <td><c:out value="${slot.startTime}"/> – <c:out value="${slot.endTime}"/></td>
                            <td data-remaining-label><c:out value="${slot.remainingCapacity}"/></td>
                            <td data-status>
                                <c:choose>
                                    <c:when test="${slot.remainingCapacity gt 0}">
                                        <span class="availability available">Còn trống</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="availability unavailable">Đã đầy</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <c:if test="${empty timeSlots or not hasAvailableSlot}">
                <p class="empty-state">Ngày này đã hết lịch. Vui lòng chọn ngày khác.</p>
            </c:if>
            <p class="date-format-hint">
                Số chỗ trên màn hình được tạo ngẫu nhiên để minh họa; hệ thống vẫn kiểm tra chỗ thực khi đặt.
            </p>

            <div class="form-actions">
                <a class="button button-quiet" href="${pageContext.request.contextPath}/">Quay lại</a>
                <button class="button" id="continue-button" type="submit" disabled>Tiếp tục</button>
            </div>
        </form>
    </section>
</main>
<script>
    const slotForm = document.getElementById("slot-form");
    const continueButton = document.getElementById("continue-button");
    slotForm.addEventListener("change", (event) => {
        if (event.target.name === "timeSlotId") {
            continueButton.disabled = false;
        }
    });
    slotForm.querySelectorAll(".slot-row").forEach((row) => {
        row.addEventListener("click", (event) => {
            if (event.target.closest("input, a, button")) {
                return;
            }
            const radio = row.querySelector('input[type="radio"]');
            if (radio && !radio.disabled) {
                radio.checked = true;
                radio.dispatchEvent(new Event("change", { bubbles: true }));
            }
        });

        const radio = row.querySelector('input[type="radio"]');
        const actualRemaining = Number(row.dataset.remaining);
        if (radio && actualRemaining > 0) {
            const displayedRemaining = Math.floor(Math.random() * (actualRemaining + 1));
            row.querySelector("[data-remaining-label]").textContent = displayedRemaining;
            if (displayedRemaining === 0) {
                row.classList.add("slot-full");
                radio.checked = false;
                radio.disabled = true;
                row.querySelector("[data-status]").innerHTML =
                    '<span class="availability unavailable">Đã đầy</span>';
            }
        }
    });
</script>
</body>
</html>
