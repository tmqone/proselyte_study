package com.tmq.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.exception.FileNotFoundException;
import com.tmq.exception.NotCorrectInputException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/v1/file")
public class FileFilterChain extends HttpFilter {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (NotCorrectInputException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        } catch (FileNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Файл не найден")));
        }
    }
}
