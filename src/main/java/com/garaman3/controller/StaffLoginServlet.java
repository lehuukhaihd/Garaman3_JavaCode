package com.garaman3.controller;

import com.garaman3.dao.Staff3DAO;
import com.garaman3.entity.StaffAccount;
import com.garaman3.util.DBContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "StaffLoginServlet", urlPatterns = "/dang-nhap")
public class StaffLoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW = "/WEB-INF/views/dang-nhap.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object staff = session == null
                ? null : session.getAttribute(StaffLoginFilter.SESSION_ACCOUNT);
        if (staff instanceof StaffAccount && ((StaffAccount) staff).canViewSupplierReports()) {
            response.sendRedirect(request.getContextPath() + "/bao-cao");
            return;
        }
        if ("required".equals(request.getParameter("error"))) {
            request.setAttribute("errorMessage", "Vui lòng đăng nhập bằng tài khoản quản lý để xem báo cáo.");
        }
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = value(request.getParameter("username"));
        String password = request.getParameter("password");
        if (username.isEmpty() || password == null || password.isEmpty()) {
            showLogin(request, response, "Vui lòng nhập tên đăng nhập và mật khẩu.", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection connection = DBContext.getConnection()) {
            StaffAccount staff = new Staff3DAO(connection).authenticate(username, password);
            if (staff == null) {
                showLogin(request, response, "Tên đăng nhập hoặc mật khẩu không đúng.", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute(StaffLoginFilter.SESSION_ACCOUNT, staff);
            response.sendRedirect(request.getContextPath() + "/bao-cao");
        } catch (SQLException ex) {
            log("Could not authenticate staff.", ex);
            showLogin(request, response, "Không thể đăng nhập lúc này. Vui lòng kiểm tra MySQL và thử lại.",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response,
            String message, int status) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    private String value(String value) {
        return value == null ? "" : value.trim();
    }
}
