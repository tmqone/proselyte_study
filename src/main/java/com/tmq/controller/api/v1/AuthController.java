package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.auth.LoginAuthRequest;
import com.tmq.dto.auth.LoginAuthResponse;
import com.tmq.dto.auth.RegisterAuthRequest;
import com.tmq.dto.auth.RegisterAuthResponse;
import com.tmq.service.AuthService;
import com.tmq.util.JacksonMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/v1/auth/*")
public class AuthController extends HttpServlet {
    private static final AuthService authService = AuthService.getInstance();
    private static final ObjectMapper objectMapper = JacksonMapperUtil.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/api/v1/auth/login")) {
            LoginAuthRequest loginAuthRequest = objectMapper.readValue(req.getInputStream(), LoginAuthRequest.class);
            LoginAuthResponse loginAuthResponse = authService.login(loginAuthRequest);
            resp.getWriter().write(objectMapper.writeValueAsString(loginAuthResponse));
        }

        if (req.getRequestURI().equals("/api/v1/auth/register")) {
            RegisterAuthRequest registerAuthRequest = objectMapper.readValue(req.getReader(), RegisterAuthRequest.class);
            RegisterAuthResponse register = authService.register(registerAuthRequest);
            resp.getWriter().write(objectMapper.writeValueAsString(register));
        }
    }
}
