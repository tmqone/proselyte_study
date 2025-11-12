package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.file.*;
import com.tmq.service.FileService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import com.tmq.validator.RequestValidator;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/admin/file/*")
public class FileAdminController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapperUtil.getObjectMapper();
    private final FileService fileService = FileService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/api/v1/admin/file/download")) {
            Map<String, Integer> paramsMap = RequestValidator
                    .validateRequestQueryNumberParams(req.getParameterMap(), "id", "user_id");
            byte[] bytes = fileService.downloadFile(new DownloadFileRequest(paramsMap.get("id"),
                    paramsMap.get("user_id")));
            resp.getOutputStream().write(bytes);
        } else if (req.getParameter("id") == null && req.getParameter("user_id") == null) {
            List<FindAllFilesResponse> all = fileService.findAll();
            if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(mapper.writeValueAsString(all));
        } else if (req.getParameter("id") != null && req.getParameter("user_id") != null) {
            Map<String, Integer> queryMap = RequestValidator
                    .validateRequestQueryNumberParams(req.getParameterMap(), "id", "user_id");
            FindFileResponse userResponse = fileService.findById(new FindFileRequest(queryMap.get("id"),
                    queryMap.get("user_id")));
            resp.getWriter().write(mapper.writeValueAsString(userResponse));
        } else if (req.getParameter("id") != null) {
            Map<String, Integer> queryMap = RequestValidator
                    .validateRequestQueryNumberParams(req.getParameterMap(), "id");
            FindFileResponse file = fileService.findByIdNoEvent(queryMap.get("id"));
            resp.getWriter().write(mapper.writeValueAsString(file));
        } else if (req.getParameter("user_id") != null) {
            Map<String, Integer> params = RequestValidator.
                    validateRequestQueryNumberParams(req.getParameterMap(), "user_id");
            List<FindAllFilesResponse> userId = fileService.findByUserIdNoEvent(params.get("user_id"));
            if (userId.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(mapper.writeValueAsString(userId));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UpdateFileRequest ufp = mapper.readValue(request.getReader(), UpdateFileRequest.class);
        UpdateFileResponse userResponse = fileService.updateByAdmin(ufp,
                JwtUtil.getUserId(request.getHeader("Authorization")));
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Integer> params = RequestValidator
                .validateRequestQueryNumberParams(request.getParameterMap(), "id", "user_id");
        fileService.deleteByAdmin(params.get("id"), params.get("user_id"), JwtUtil.getUserId(request.getHeader("Authorization")));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
