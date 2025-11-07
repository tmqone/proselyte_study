package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.user.*;
import com.tmq.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/user")
public class UserController extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateUserRequest createUserRequest = mapper.readValue(req.getReader(), CreateUserRequest.class);
        CreateUserResponse user = userService.save(createUserRequest);
        resp.getWriter().write(mapper.writeValueAsString(user));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateUserRequest updateUserRequest = mapper.readValue(req.getReader(), UpdateUserRequest.class);
        UpdateUserResponse update = userService.update(updateUserRequest);
        resp.getWriter().write(mapper.writeValueAsString(update));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeleteUserRequest deleteUserRequest = mapper.readValue(req.getReader(), DeleteUserRequest.class);
        userService.delete(deleteUserRequest);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (req.getParameter("id") != null) {
            FindUserByIdResponse user = userService.findById(Integer.parseInt(id));
            resp.getWriter().write(mapper.writeValueAsString(user));
        } else {
            List<FindAllUserResponse> all = userService.findAll();
            resp.getWriter().write(mapper.writeValueAsString(all));
        }
    }
}
