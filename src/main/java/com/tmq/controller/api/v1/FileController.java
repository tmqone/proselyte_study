package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.file.*;
import com.tmq.service.FileService;
import com.tmq.util.JacksonMapper;
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

@WebServlet("/api/v1/file")
@MultipartConfig
public class FileController extends HttpServlet {
    private final ObjectMapper mapper = JacksonMapper.getObjectMapper();
    private final FileService fileService = FileService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestValidator.validateRequestNumberHeadersParams(req.getHeaderNames(), "user_id");
        if (req.getParameter("id") == null) {
            RequestValidator.validateRequestNumberHeadersParams(req.getHeaderNames(), "user_id");
            List<FindAllFilesResponse> all = fileService.findAll(req.getIntHeader("user_id"));
            resp.getWriter().write(mapper.writeValueAsString(all));
        } else {
            Map<String, Integer> queryMap = RequestValidator
                    .validateRequestQueryNumberParams(req.getParameterMap(), "id");
            FindFileResponse userResponse = fileService.findById(new FindFileRequest(queryMap.get("id"), req.getIntHeader("user_id")));
            resp.getWriter().write(mapper.writeValueAsString(userResponse));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestValidator.validateRequestNumberHeadersParams(request.getHeaderNames(), "user_id");
        Collection<Part> parts = request.getParts();
        CreateFileRequest createFileRequest = new CreateFileRequest(request.getIntHeader("user_id"), parts);
        CreateFileResponse userResponse = fileService.save(createFileRequest);
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestValidator.validateRequestNumberHeadersParams(request.getHeaderNames(), "user_id");
        UpdateFilePreRequest ufp = mapper.readValue(request.getReader(), UpdateFilePreRequest.class);
        UpdateFileRequest updateFileRequest = new UpdateFileRequest(ufp.id(), request.getIntHeader("user_id"), ufp.name());
        UpdateFileResponse userResponse = fileService.update(updateFileRequest);
        response.getWriter().write(mapper.writeValueAsString(userResponse));
    }

    @Override
    //TODO переделать получение user_id в header
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DeleteFileRequest deleteFileRequest = mapper.readValue(request.getReader(), DeleteFileRequest.class);
        fileService.delete(new DeleteFileRequest(deleteFileRequest.id(), deleteFileRequest.userId()));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
