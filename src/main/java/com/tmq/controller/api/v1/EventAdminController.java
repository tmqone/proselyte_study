package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.dto.event.*;
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
        if (parameterMap.containsKey("id")) {
            Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "id");
            FindEventByIdResponse response = eventService.findById(params.get("id"));
            resp.getWriter().write(objectMapper.writeValueAsString(response));
        } else if (parameterMap.containsKey("file_id")) {
            Map<String, Integer> params = RequestValidator.validateRequestQueryNumberParams(parameterMap, "file_id");
            List<FindAllEventResponse> all = eventService.findByFileId(
                    Integer.valueOf(req.getParameter("file_id"))
            );
            resp.getWriter().write(objectMapper.writeValueAsString(all));
        } else {
            List<FindAllEventResponse> all = eventService.findAll();
            if (all.isEmpty()) resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().write(objectMapper.writeValueAsString(all));
        }
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
