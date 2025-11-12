package com.tmq.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class HeadersFilterChain extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getRequestURI().equals("/api/v1/file/download") || req.getRequestURI().equals("/api/v1/admin/file/download")) {
            res.setContentType("application/octet-stream");
        } else {
            res.setContentType("application/json");
        }
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Max-Age", "3600");
        super.doFilter(req, res, chain);
    }
}
