package com.garaman3.controller;

import com.garaman3.dao.ImportInvoice3DAO;
import com.garaman3.dao.ImportInvoiceDetail3DAO;
import com.garaman3.dao.SupplierStatistics3DAO;
import com.garaman3.entity.ImportInvoice3;
import com.garaman3.entity.ImportInvoiceDetail3;
import com.garaman3.entity.Supplier3;
import com.garaman3.entity.SupplierStatistics3;
import com.garaman3.util.DBContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SupplierStatisticsServlet", urlPatterns = {
    "/bao-cao",
    "/thong-ke-nha-cung-cap",
    "/bao-cao/nha-cung-cap",
    "/bao-cao/nha-cung-cap/nhap-hang",
    "/bao-cao/hoa-don-nhap"
})
public class SupplierStatisticsServlet extends HttpServlet {

    private static final DateTimeFormatter VIETNAMESE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final String REPORTS_VIEW = "/WEB-INF/views/bao-cao.jsp";
    private static final String PERIOD_VIEW = "/WEB-INF/views/thoi-gian-thong-ke.jsp";
    private static final String STATISTICS_VIEW = "/WEB-INF/views/thong-ke-nha-cung-cap.jsp";
    private static final String IMPORTS_VIEW = "/WEB-INF/views/danh-sach-nhap-hang.jsp";
    private static final String INVOICE_VIEW = "/WEB-INF/views/chi-tiet-hoa-don-nhap.jsp";
    private static final String ERROR_VIEW = "/WEB-INF/views/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/bao-cao".equals(path)) {
            request.getRequestDispatcher(REPORTS_VIEW).forward(request, response);
        } else if ("/thong-ke-nha-cung-cap".equals(path)) {
            showPeriod(request, response, null, null, null);
        } else if ("/bao-cao/nha-cung-cap".equals(path)) {
            showStatistics(request, response);
        } else if ("/bao-cao/nha-cung-cap/nhap-hang".equals(path)) {
            showImports(request, response);
        } else if ("/bao-cao/hoa-don-nhap".equals(path)) {
            showInvoice(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!"/thong-ke-nha-cung-cap".equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        LocalDate startDate = parseDate(request.getParameter("startDate"));
        LocalDate endDate = parseDate(request.getParameter("endDate"));
        if (startDate == null || endDate == null) {
            showPeriod(request, response, request.getParameter("startDate"),
                    request.getParameter("endDate"), "Vui lòng nhập ngày bắt đầu và ngày kết thúc hợp lệ.");
            return;
        }
        if (endDate.isBefore(startDate)) {
            showPeriod(request, response, startDate.toString(), endDate.toString(),
                    "Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.");
            return;
        }

        response.sendRedirect(request.getContextPath()
                + "/bao-cao/nha-cung-cap?startDate=" + startDate + "&endDate=" + endDate);
    }

    private void showPeriod(HttpServletRequest request, HttpServletResponse response,
            String startValue, String endValue, String message) throws ServletException, IOException {
        LocalDate today = LocalDate.now();
        request.setAttribute("startDate", inputDate(
                startValue == null ? today.withDayOfMonth(1).toString() : startValue));
        request.setAttribute("endDate", inputDate(endValue == null ? today.toString() : endValue));
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher(PERIOD_VIEW).forward(request, response);
    }

    private void showStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDate startDate = parseDate(request.getParameter("startDate"));
        LocalDate endDate = parseDate(request.getParameter("endDate"));
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            String message = endDate != null && startDate != null
                    ? "Ngày kết thúc phải bằng hoặc sau ngày bắt đầu."
                    : "Vui lòng nhập khoảng ngày thống kê hợp lệ.";
            showPeriod(request, response, request.getParameter("startDate"),
                    request.getParameter("endDate"), message);
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            List<SupplierStatistics3> statistics =
                    new SupplierStatistics3DAO(connection).getSupplierStatistics(startDate, endDate);
            request.setAttribute("statistics", statistics);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.getRequestDispatcher(STATISTICS_VIEW).forward(request, response);
        } catch (SQLException ex) {
            log("Could not load supplier statistics.", ex);
            showDatabaseError(request, response, "Không thể tải thống kê nhà cung cấp. Vui lòng kiểm tra database.");
        }
    }

    private void showImports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDate startDate = parseDate(request.getParameter("startDate"));
        LocalDate endDate = parseDate(request.getParameter("endDate"));
        String supplierId = value(request.getParameter("supplierId"));
        if (!validPeriod(startDate, endDate) || supplierId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing supplier or date range.");
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            ImportInvoice3DAO invoiceDAO = new ImportInvoice3DAO(connection);
            Supplier3 supplier = invoiceDAO.getSupplierById(supplierId);
            if (supplier == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Supplier not found.");
                return;
            }

            List<ImportInvoice3> invoices = invoiceDAO.getImportsBySupplier(supplier, startDate, endDate);
            request.setAttribute("supplier", supplier);
            request.setAttribute("invoices", invoices);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.getRequestDispatcher(IMPORTS_VIEW).forward(request, response);
        } catch (SQLException ex) {
            log("Could not load supplier import invoices.", ex);
            showDatabaseError(request, response, "Không thể tải danh sách hóa đơn nhập. Vui lòng thử lại.");
        }
    }

    private void showInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDate startDate = parseDate(request.getParameter("startDate"));
        LocalDate endDate = parseDate(request.getParameter("endDate"));
        String supplierId = value(request.getParameter("supplierId"));
        String invoiceId = value(request.getParameter("invoiceId"));
        if (!validPeriod(startDate, endDate) || supplierId.isEmpty() || invoiceId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing invoice or date range.");
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            ImportInvoice3DAO invoiceDAO = new ImportInvoice3DAO(connection);
            Supplier3 supplier = invoiceDAO.getSupplierById(supplierId);
            if (supplier == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Supplier not found.");
                return;
            }

            ImportInvoice3 invoice = null;
            for (ImportInvoice3 candidate : invoiceDAO.getImportsBySupplier(supplier, startDate, endDate)) {
                if (candidate.getId().equals(invoiceId)) {
                    invoice = candidate;
                    break;
                }
            }
            if (invoice == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Import invoice not found in this period.");
                return;
            }

            List<ImportInvoiceDetail3> details =
                    new ImportInvoiceDetail3DAO(connection).getDetailsByInvoice(invoice);
            request.setAttribute("supplier", supplier);
            request.setAttribute("invoice", invoice);
            request.setAttribute("details", details);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.getRequestDispatcher(INVOICE_VIEW).forward(request, response);
        } catch (SQLException ex) {
            log("Could not load import invoice details.", ex);
            showDatabaseError(request, response, "Không thể tải chi tiết hóa đơn nhập. Vui lòng thử lại.");
        }
    }

    private boolean validPeriod(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            try {
                return LocalDate.parse(value, VIETNAMESE_DATE_FORMAT);
            } catch (DateTimeParseException invalidVietnameseDate) {
                return null;
            }
        }
    }

    private String inputDate(String value) {
        LocalDate date = parseDate(value);
        return date == null ? "" : date.toString();
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
