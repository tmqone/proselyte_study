package com.tmq.filter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.tmq.dto.exception.ExceptionDto;
import com.tmq.exception.*;
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
        } catch (UserNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Пользователь не найден")));
        } catch (NotCorrectInputException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        } catch (FileNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Файл не найден")));
        } catch (FileExistsException e) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Файл с таким именем уже существует")));
        } catch (EventNotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto("Событие не найдено")));
        } catch (GeneralException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getMessage())));
        } catch (ConstraintViolationException e) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(
                    switch (e.getConstraintName()) {
                        case "users_username_key" -> "Пользователь с таким именем уже существует";
                        case "events_user_id_fkey" -> "Пользователь не может быть удалён, т.к. у него имеются связанные сущности";
                        case "events_file_id_fkey" -> "Файл не может быть удалён т.к. у него имеются связанные сущности";
                        default -> "Ошибка при сохранении в БД";
                    }
            )));
        } catch (JacksonException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(mapper.writeValueAsString(new ExceptionDto(e.getOriginalMessage().split(";")[0])));
        }
    }
}
