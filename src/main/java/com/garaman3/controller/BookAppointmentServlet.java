package com.garaman3.controller;

import com.garaman3.dao.Appointment3DAO;
import com.garaman3.dao.Customer3DAO;
import com.garaman3.dao.TimeSlot3DAO;
import com.garaman3.dao.Vehicle3DAO;
import com.garaman3.entity.Appointment3;
import com.garaman3.entity.Customer3;
import com.garaman3.entity.TimeSlot3;
import com.garaman3.entity.Vehicle3;
import com.garaman3.util.DBContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BookAppointmentServlet", urlPatterns = {
    "/dat-lich", "/thong-tin-khach", "/xac-nhan-dat-lich"
})
public class BookAppointmentServlet extends HttpServlet {

    private static final String TIME_VIEW = "/WEB-INF/views/dat-lich.jsp";
    private static final String CUSTOMER_VIEW = "/WEB-INF/views/thong-tin-khach.jsp";
    private static final String SUCCESS_VIEW = "/WEB-INF/views/thanh-cong.jsp";
    private static final String ERROR_VIEW = "/WEB-INF/views/error.jsp";
    private static final String SESSION_DATE = "appointmentDate";
    private static final String SESSION_SLOT_ID = "appointmentTimeSlotId";
    private static final String SESSION_LAST_APPOINTMENT = "lastAppointment";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/dat-lich".equals(path)) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDate date = parseDate(request.getParameter("date"), tomorrow);
            String message = null;
            if (date.isBefore(tomorrow)) {
                date = tomorrow;
                message = "Vui lòng chọn ngày từ ngày mai trở đi.";
            }
            showTimeSelection(request, response, date, message, null);
        } else if ("/thong-tin-khach".equals(path)) {
            showCustomerInformation(request, response, null);
        } else if ("/xac-nhan-dat-lich".equals(path)) {
            showSuccess(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        if ("/thong-tin-khach".equals(path)) {
            continueToCustomerInformation(request, response);
        } else if ("/xac-nhan-dat-lich".equals(path)) {
            confirmAppointment(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void continueToCustomerInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate date = parseDate(request.getParameter("date"), null);
        String slotId = value(request.getParameter("timeSlotId"));
        if (date == null || date.isBefore(tomorrow) || slotId.isEmpty()) {
            showTimeSelection(request, response, tomorrow,
                    "Chọn một ngày từ ngày mai trở đi và một khung giờ còn trống.", slotId);
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            TimeSlot3 slot = findSlot(new TimeSlot3DAO(connection).getSlotsByDate(date), slotId);
            if (slot == null || slot.getRemainingCapacity() <= 0) {
                showTimeSelection(request, response, date,
                        "Khung giờ bạn chọn vừa hết chỗ. Vui lòng chọn lại.", slotId);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute(SESSION_DATE, date);
            session.setAttribute(SESSION_SLOT_ID, slotId);
            response.sendRedirect(request.getContextPath() + "/thong-tin-khach");
        } catch (SQLException ex) {
            log("Could not verify the selected appointment time.", ex);
            showDatabaseError(request, response, "Không thể kiểm tra khung giờ. Vui lòng thử lại.");
        }
    }

    private void showCustomerInformation(HttpServletRequest request, HttpServletResponse response,
            String message) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        LocalDate date = session == null ? null : (LocalDate) session.getAttribute(SESSION_DATE);
        String slotId = session == null ? null : (String) session.getAttribute(SESSION_SLOT_ID);
        if (date == null || slotId == null) {
            response.sendRedirect(request.getContextPath() + "/dat-lich");
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            TimeSlot3 slot = findSlot(new TimeSlot3DAO(connection).getSlotsByDate(date), slotId);
            if (slot == null || slot.getRemainingCapacity() <= 0) {
                session.removeAttribute(SESSION_DATE);
                session.removeAttribute(SESSION_SLOT_ID);
                showTimeSelection(request, response, date,
                        "Khung giờ vừa hết chỗ. Vui lòng chọn lại.", slotId);
                return;
            }
            request.setAttribute("appointmentDate", date);
            request.setAttribute("timeSlot", slot);
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher(CUSTOMER_VIEW).forward(request, response);
        } catch (SQLException ex) {
            log("Could not reload the selected appointment time.", ex);
            showDatabaseError(request, response, "Không thể tải thông tin khung giờ. Vui lòng thử lại.");
        }
    }

    private void confirmAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = value(request.getParameter("fullName"));
        String phone = value(request.getParameter("phone"));
        String licensePlate = value(request.getParameter("licensePlate"));
        String note = value(request.getParameter("note"));
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        HttpSession session = request.getSession(false);
        LocalDate date = session == null ? null : (LocalDate) session.getAttribute(SESSION_DATE);
        String slotId = session == null ? null : (String) session.getAttribute(SESSION_SLOT_ID);

        if (date == null || slotId == null) {
            response.sendRedirect(request.getContextPath() + "/dat-lich");
            return;
        }
        if (date.isBefore(tomorrow)
                || fullName.isEmpty()
                || phone.isEmpty()
                || licensePlate.isEmpty()
                || fullName.length() > 100
                || phone.length() > 15
                || licensePlate.length() > 15
                || note.length() > 255) {
            showCustomerInformation(request, response,
                    "Vui lòng nhập đầy đủ và kiểm tra lại thông tin bắt buộc.");
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            connection.setAutoCommit(false);
            try {
                TimeSlot3DAO timeSlotDAO = new TimeSlot3DAO(connection);
                Appointment3DAO appointmentDAO = new Appointment3DAO(connection);
                if (!timeSlotDAO.lockSlot(slotId)) {
                    connection.rollback();
                    session.removeAttribute(SESSION_DATE);
                    session.removeAttribute(SESSION_SLOT_ID);
                    showTimeSelection(request, response, date,
                            "Khung giờ không còn tồn tại. Vui lòng chọn lại.", slotId);
                    return;
                }

                TimeSlot3 slot = findSlot(timeSlotDAO.getSlotsByDate(date), slotId);
                if (slot == null || slot.getRemainingCapacity() <= 0
                        || !appointmentDAO.isSlotAvailable(date, slot)) {
                    connection.rollback();
                    session.removeAttribute(SESSION_DATE);
                    session.removeAttribute(SESSION_SLOT_ID);
                    showTimeSelection(request, response, date,
                            "Khung giờ vừa hết chỗ. Vui lòng chọn lại.", slotId);
                    return;
                }

                if (appointmentDAO.existsBookedOnDate(licensePlate, date)) {
                    connection.rollback();
                    showCustomerInformation(request, response,
                            "Xe đã có lịch hẹn, không cần tạo thêm lịch hẹn.");
                    return;
                }

                Customer3 customer = new Customer3DAO(connection).findOrCreateByPhone(fullName, phone);
                Vehicle3 vehicle = new Vehicle3DAO(connection).findOrCreateByPlate(licensePlate, customer);
                Appointment3 appointment = appointmentDAO.saveAppointment(date, slot, customer, vehicle, note);
                connection.commit();
                session.removeAttribute(SESSION_DATE);
                session.removeAttribute(SESSION_SLOT_ID);
                session.setAttribute(SESSION_LAST_APPOINTMENT, appointment);
                response.sendRedirect(request.getContextPath() + "/xac-nhan-dat-lich");
            } catch (SQLException | RuntimeException ex) {
                connection.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            log("Could not save appointment.", ex);
            showDatabaseError(request, response, "Không thể lưu lịch hẹn. Vui lòng thử lại sau.");
        }
    }

    private void showTimeSelection(HttpServletRequest request, HttpServletResponse response,
            LocalDate date, String message, String selectedSlotId) throws ServletException, IOException {
        try (Connection connection = DBContext.getConnection()) {
            List<TimeSlot3> slots = new TimeSlot3DAO(connection).getSlotsByDate(date);
            request.setAttribute("appointmentDate", date);
            request.setAttribute("minimumDate", LocalDate.now().plusDays(1));
            request.setAttribute("timeSlots", slots);
            request.setAttribute("selectedSlotId", selectedSlotId);
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher(TIME_VIEW).forward(request, response);
        } catch (SQLException ex) {
            log("Could not load appointment time slots.", ex);
            showDatabaseError(request, response, "Không thể tải khung giờ. Vui lòng kiểm tra MySQL và cấu hình database.");
        }
    }

    private void showSuccess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Appointment3 appointment = session == null
                ? null : (Appointment3) session.getAttribute(SESSION_LAST_APPOINTMENT);
        if (appointment == null) {
            response.sendRedirect(request.getContextPath() + "/dat-lich");
            return;
        }
        session.removeAttribute(SESSION_LAST_APPOINTMENT);
        request.setAttribute("appointment", appointment);
        request.getRequestDispatcher(SUCCESS_VIEW).forward(request, response);
    }

    private TimeSlot3 findSlot(List<TimeSlot3> slots, String slotId) {
        for (TimeSlot3 slot : slots) {
            if (slot.getId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return fallback;
        }
    }

    private String value(String value) {
        return value == null ? "" : value.trim();
    }

    private void showDatabaseError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }
}
