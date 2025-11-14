package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.user.*;
import com.tmq.service.UserService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/v1/user")
public class UserController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapperUtil.getObjectMapper();
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = JwtUtil.getUserId(req.getHeader("Authorization"));
        FindUserByIdResponse user = userService.findById(userId);
        mapper.writeValue(resp.getOutputStream(), user);
    }
}
