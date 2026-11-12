package com.garaman3.controller;

import com.garaman3.entity.StaffAccount;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/bao-cao", "/bao-cao/*", "/thong-ke-nha-cung-cap"})
public class StaffLoginFilter implements Filter {

    public static final String SESSION_ACCOUNT = "authenticatedStaff";
    private static final String NO_ACCESS_VIEW = "/WEB-INF/views/khong-co-quyen.jsp";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        Object value = session == null ? null : session.getAttribute(SESSION_ACCOUNT);

        httpResponse.setHeader("Cache-Control", "no-store");
        if (!(value instanceof StaffAccount)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dang-nhap");
            return;
        }

        StaffAccount staff = (StaffAccount) value;
        if (!staff.canViewSupplierReports()) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpRequest.getRequestDispatcher(NO_ACCESS_VIEW).forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
