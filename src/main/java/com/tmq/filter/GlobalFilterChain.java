package com.tmq.filter;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.exception.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class GlobalFilterChain extends HttpFilter {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
//        } catch (DatabindException e) {
//            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Некорректное тело запроса")));
        } catch (UserNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Пользователь не найден")));
        }
    }
}
