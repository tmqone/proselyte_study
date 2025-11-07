package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.tmq.dto.file.*;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.service.FileService;
import com.tmq.util.FileUtil;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/file")
@MultipartConfig
public class FileController extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> paramsMap = RequestValidator.
                validateRequestNumberParams(req.getParameterMap(), "id", "user_id");
        FindFileResponse file = fileService.findById(new FindFileRequest(paramsMap.get("id"), paramsMap.get("user_id")));
        File response = new File(file.filePath());
        resp.getOutputStream().write(Files.readAllBytes(response.toPath()));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Integer> params = RequestValidator
                .validateRequestNumberParams(request.getParameterMap(), "user_id");
        Collection<Part> parts = request.getParts();
        List<CreateFileRequest> createFileRequests = FileUtil.saveFiles(parts, params.get("user_id"));
        List<CreateFileResponse> userResponse = createFileRequests.stream().map(fileService::save).toList();
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Integer> params = RequestValidator
                .validateRequestNumberParams(request.getParameterMap(), "user_id", "id");
        Collection<Part> parts = request.getParts();
        UpdateFileRequest createFileRequests = FileUtil.updateFile(params.get("user_id"), params.get("id"), parts);
        UpdateFileResponse userResponse = fileService.update(createFileRequests);
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
