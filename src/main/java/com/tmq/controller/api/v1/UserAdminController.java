package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.user.*;
import com.tmq.model.Role;
import com.tmq.service.UserService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/admin/user/*")
public class UserAdminController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapperUtil.getObjectMapper();
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
        Map<String, Integer> params = RequestValidator
                .validateRequestQueryNumberParams(req.getParameterMap(), "id");
        userService.delete(params.get("id"));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (req.getParameter("id") != null) {
            Map<String, Integer> params = RequestValidator
                    .validateRequestQueryNumberParams(req.getParameterMap(), "id");
            FindUserByIdResponse user = userService.findById(params.get("id"));
            resp.getWriter().write(mapper.writeValueAsString(user));
        } else {
            List<FindAllUserResponse> all = userService.findAll();
            if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(mapper.writeValueAsString(all));
        }
    }
}
