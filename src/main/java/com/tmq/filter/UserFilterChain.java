package com.tmq.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.exception.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;

@WebFilter("/api/v1/user")
public class UserFilterChain extends HttpFilter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (UserNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(objectMapper.writeValueAsString(new ExceptionDto("Пользователь не найден")));
        } catch (ConstraintViolationException e){
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(objectMapper.writeValueAsString(new ExceptionDto("Пользователь уже создан")));
        }
    }
}
