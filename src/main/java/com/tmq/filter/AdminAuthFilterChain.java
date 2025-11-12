package com.tmq.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.model.Role;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/v1/admin/*")
public class AdminAuthFilterChain extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            if (JwtUtil.getUserRole(req.getHeader("Authorization")).equals(Role.ADMIN)){
                super.doFilter(req, res, chain);
            } else {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
