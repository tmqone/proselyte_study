package com.tmq.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.tmq.dto.event.*;
import com.tmq.mapper.EventMapper;
import com.tmq.service.EventService;
import com.tmq.validator.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/event")
public class EventController extends HttpServlet {
    private final EventService eventService = new EventService();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (!parameterMap.containsKey("id")) {
            List<FindAllEventResponse> all = eventService.findAll();
            resp.getWriter().write(objectMapper.writeValueAsString(all));
        } else {
            Map<String, Integer> params = RequestValidator.validateRequestNumberParams(parameterMap, "id");
            FindEventByIdResponse id = eventService.findById(params.get("id"));
            resp.getWriter().write(objectMapper.writeValueAsString(id));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateEventRequest createEventRequest = objectMapper.readValue(req.getReader(), CreateEventRequest.class);
        CreateEventResponse save = eventService.save(createEventRequest);
        resp.getWriter().write(objectMapper.writeValueAsString(save));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateEventRequest updateEventRequest = objectMapper.readValue(req.getReader(), UpdateEventRequest.class);
        UpdateEventResponse userResponse = eventService.update(updateEventRequest);
        resp.getWriter().write(objectMapper.writeValueAsString(userResponse));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeleteEventRequest deleteEventRequest = objectMapper.readValue(req.getReader(), DeleteEventRequest.class);
        eventService.delete(deleteEventRequest);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
