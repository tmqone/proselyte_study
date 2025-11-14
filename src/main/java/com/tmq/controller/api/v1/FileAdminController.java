package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.file.*;
import com.tmq.exception.NotCorrectInputException;
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
        Map<String, String[]> parameterMap = req.getParameterMap();
        String uri = req.getRequestURI();

        if (uri.equals("/api/v1/admin/file/download")) {
            Map<String, Integer> paramsMap = RequestValidator
                    .validateRequestQueryNumberParams(parameterMap, "id", "user_id");
            byte[] bytes = fileService.downloadFile(new DownloadFileRequest(paramsMap.get("id"),
                    paramsMap.get("user_id")));
            resp.getOutputStream().write(bytes);
            return;
        }

        String requestType = checkRequestType(req);

        Object result = switch (requestType) {
            case "all" -> fileService.findAll();
            case "id_and_user" -> {
                Map<String, Integer> queryMap = RequestValidator
                        .validateRequestQueryNumberParams(parameterMap, "id", "user_id");
                yield fileService.findById(new FindFileRequest(queryMap.get("id"), queryMap.get("user_id")));
            }
            case "id_only" -> {
                Map<String, Integer> queryMap = RequestValidator
                        .validateRequestQueryNumberParams(parameterMap, "id");
                yield fileService.findByIdNoEvent(queryMap.get("id"));
            }
            case "user_only" -> {
                Map<String, Integer> params = RequestValidator
                        .validateRequestQueryNumberParams(parameterMap, "user_id");
                yield fileService.findByUserIdNoEvent(params.get("user_id"));
            }
            default -> throw new NotCorrectInputException();
        };

        if (result instanceof List<?> list) {
            if (list.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
        }
        
        resp.getWriter().write(mapper.writeValueAsString(result));
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

    private String checkRequestType(HttpServletRequest req) {
        boolean hasId = req.getParameter("id") != null;
        boolean hasUserId = req.getParameter("user_id") != null;

        if (!hasId && !hasUserId) {
            return "all";
        } else if (hasId && hasUserId) {
            return "id_and_user";
        } else if (hasId) {
            return "id_only";
        } else {
            return "user_only";
        }
    }
}
