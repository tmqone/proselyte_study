package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.user.*;
import com.tmq.service.UserService;
import com.tmq.util.JacksonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/user")
public class UserController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapper.getObjectMapper();
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUserRequest createUserRequest = mapper.readValue(req.getReader(), CreateUserRequest.class);
        CreateUserResponse user = userService.save(createUserRequest);
        resp.getWriter().write(mapper.writeValueAsString(user));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UpdateUserRequest updateUserRequest = mapper.readValue(req.getReader(), UpdateUserRequest.class);
        UpdateUserResponse update = userService.update(updateUserRequest);
        resp.getWriter().write(mapper.writeValueAsString(update));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DeleteUserRequest deleteUserRequest = mapper.readValue(req.getReader(), DeleteUserRequest.class);
        userService.delete(deleteUserRequest);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
