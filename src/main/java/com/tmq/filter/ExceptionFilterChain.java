package com.tmq.filter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.exception.*;
import com.tmq.processor.ConstraintExceptionProccesor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilterChain extends HttpFilter {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch(UserNotFoundException e){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Пользователь не найден")));
        } catch(NotCorrectInputException e){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        } catch(FileNotFoundException e){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Файл не найден")));
        } catch(FileExistsException e){
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Файл с таким именем уже существует")));
        } catch(EventNotFoundException e){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Событие не найдено")));
        } catch(UserExistsException e){
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Пользователь с таким именем уже сущестсвует")));
        } catch(AuthException e){
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        } catch(ConstraintViolationException e){
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(ConstraintExceptionProccesor.process(e))));
        } catch(JacksonException e){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getOriginalMessage().split(";")[0])));
        } catch(ServletException e){
            if (e.getMessage().contains("InvalidContentTypeException")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        }
    }
}
