package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.file.*;
import com.tmq.service.FileService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/file/*")
@MultipartConfig
public class FileController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapperUtil.getObjectMapper();
    private final FileService fileService = FileService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        int userId = JwtUtil.getUserId(req.getHeader("Authorization"));

        switch (uri) {
            case "/api/v1/file/download" -> {
                Map<String, Integer> paramsMap = RequestValidator
                        .validateRequestQueryNumberParams(req.getParameterMap(), "id");
                byte[] bytes = fileService.downloadFile(new DownloadFileRequest(paramsMap.get("id"), userId));
                resp.getOutputStream().write(bytes);
            }
            default -> {
                List<FindAllFilesResponse> all = fileService.findAllFilesByUserId(userId);

                if (req.getParameter("id") != null) {
                    Map<String, Integer> queryMap = RequestValidator
                            .validateRequestQueryNumberParams(req.getParameterMap(), "id");
                    FindFileResponse userResponse = fileService.findById(new FindFileRequest(queryMap.get("id"), userId));
                    resp.getWriter().write(mapper.writeValueAsString(userResponse));
                } else {
                    if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                    resp.getWriter().write(mapper.writeValueAsString(all));
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Part> parts = request.getParts();
        CreateFileRequest createFileRequest = new CreateFileRequest(
                JwtUtil.getUserId(request.getHeader("Authorization")),parts);
        CreateFileResponse userResponse = fileService.save(createFileRequest);
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UpdateFilePreRequest ufp = mapper.readValue(request.getReader(), UpdateFilePreRequest.class);
        UpdateFileRequest updateFileRequest = new UpdateFileRequest(ufp.id(),
                JwtUtil.getUserId(request.getHeader("Authorization")), ufp.name());
        UpdateFileResponse userResponse = fileService.update(updateFileRequest);
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Integer> paramsMap = RequestValidator
                .validateRequestQueryNumberParams(request.getParameterMap(), "id");
        fileService.delete(new DeleteFileRequest(paramsMap.get("id"),
                JwtUtil.getUserId(request.getHeader("Authorization"))));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
