package com.tmq.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/api/v1/file/*",
        "/api/v1/event/*",
        "/api/v1/user/*"
})
public class AuthFilterChain extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String authorization = req.getHeader("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.doFilter(req, res, chain);
        }
    }
}
