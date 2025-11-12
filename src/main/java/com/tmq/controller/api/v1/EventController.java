package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.event.*;
import com.tmq.service.EventService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.util.JwtUtil;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/event/*")
public class EventController extends HttpServlet {
    private final ObjectMapper objectMapper = JacksonMapperUtil.getObjectMapper();
    private static final EventService eventService = EventService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("id")) {
            Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "id");
            FindEventByIdResponse response = eventService.findByIdByUser(params.get("id"),
                    JwtUtil.getUserId(req.getHeader("Authorization")));
            resp.getWriter().write(objectMapper.writeValueAsString(response));
        } else if (parameterMap.containsKey("file_id")) {
            Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "file_id");
            List<FindAllEventResponse> all = eventService.findAllByUserIdWithFileId(
                    JwtUtil.getUserId(req.getHeader("Authorization")),
                    Integer.valueOf(req.getParameter("file_id"))
            );
            if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(objectMapper.writeValueAsString(all));
        } else {
            List<FindAllEventResponse> all = eventService.findAllByUserId(
                    JwtUtil.getUserId(req.getHeader("Authorization")));
            if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(objectMapper.writeValueAsString(all));
        }
    }
}
