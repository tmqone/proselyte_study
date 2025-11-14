package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.event.*;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.service.EventService;
import com.tmq.util.JacksonMapperUtil;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/admin/event")
public class EventAdminController extends HttpServlet {
    private final ObjectMapper objectMapper = JacksonMapperUtil.getObjectMapper();
    private static final EventService eventService = EventService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();

        String requestType = parameterMap.containsKey("id") ? "id" :
                parameterMap.containsKey("file_id") ? "file_id" : "all";

        Object result = switch (requestType) {
            case "id" -> {
                Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "id");
                yield eventService.findById(params.get("id"));
            }
            case "file_id" -> {
                Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "file_id");
                yield eventService.findByFileId(params.get("file_id"));
            }
            case "all" -> eventService.findAll();
            default -> throw new NotCorrectInputException();
        };

        if (result instanceof List<?> list) {
            if (list.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
        }
        resp.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateEventRequest createEventRequest = objectMapper.readValue(req.getReader(), CreateEventRequest.class);
        CreateEventResponse save = eventService.save(createEventRequest);
        resp.getWriter().write(objectMapper.writeValueAsString(save));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UpdateEventRequest updateEventRequest = objectMapper.readValue(req.getReader(), UpdateEventRequest.class);
        UpdateEventResponse userResponse = eventService.update(updateEventRequest);
        resp.getWriter().write(objectMapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> params = RequestValidator
                .validateRequestQueryNumberParams(req.getParameterMap(), "id");
        eventService.delete(params.get("id"));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
