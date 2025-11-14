package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.event.*;
import com.tmq.exception.NotCorrectInputException;
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
        Integer userId = JwtUtil.getUserId(req.getHeader("Authorization"));

        String requestType = parameterMap.containsKey("id") ? "id" :
                parameterMap.containsKey("file_id") ? "file_id" : "all";

        Object result = switch (requestType) {
            case "id" -> {
                Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "id");
                yield eventService.findByIdByUser(params.get("id"), userId);
            }
            case "file_id" -> {
                Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "file_id");
                yield eventService.findAllByUserIdWithFileId(userId, params.get("file_id"));
            }
            case "all" -> eventService.findAllByUserId(userId);
            default -> throw new NotCorrectInputException();
        };

        if (result instanceof List<?> list && list.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.getWriter().write(objectMapper.writeValueAsString(result));
        }
    }
}
