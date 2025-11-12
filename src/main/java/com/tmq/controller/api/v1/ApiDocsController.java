package com.tmq.controller.api.v1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/api/v1/api-docs")
public class ApiDocsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.getWriter().write(Files
                .readString(Path.of(ApiDocsController.class.getClassLoader().getResource("swagger.yml").getPath())));
    }
}
