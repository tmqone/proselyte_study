package com.tmq.mapper;

import com.tmq.dto.entity.EventDto;
import com.tmq.dto.entity.EventWithoutUserDto;
import com.tmq.dto.event.*;
import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;

import java.util.List;

public class EventMapper {

    public static EventDto toEventDto(Event event){
        return new EventDto(event.getId(),
                UserMapper.toUserDto(event.getUser()),
                FileMapper.toFileDto(event.getFile()),
                event.getAction());
    }

    public static EventWithoutUserDto toEventWithoutUserEventsDto(Event event){
        return new EventWithoutUserDto(
                event.getId(),
                FileMapper.toFileDto(event.getFile()),
                event.getAction()
        );
    }

    public Event postToEntity(CreateEventRequest dto) {
        return Event.builder()
                .file(File.builder().id(dto.fileId()).build())
                .user(User.builder().id(dto.userId()).build())
                .action(dto.action())
                .build();
    }

    public CreateEventResponse postFromEntity(Event event) {
        return new CreateEventResponse(
                event.getId(), UserMapper.toUserWithoutEventDto(event.getUser()),
                FileMapper.toFileDto(event.getFile()), event.getAction()
        );
    }

    public FindEventByIdResponse getByIdFromEntity(Event event) {
        return new FindEventByIdResponse(event.getId(), UserMapper.toUserWithoutEventDto(event.getUser()),
                FileMapper.toFileDto(event.getFile()), event.getAction());
    }

    public Event updateToEntity(UpdateEventRequest dto) {
        return Event.builder().id(dto.id())
                .user(User.builder().id(dto.userId()).build())
                .file(File.builder().id(dto.fileId()).build())
                .action(dto.action()).build();
    }

    public List<FindAllEventResponse> findAllFromEntity(List<Event> event) {
        return event.stream()
                .map(value -> new FindAllEventResponse(value.getId(), UserMapper.toUserWithoutEventDto(value.getUser()),
                        FileMapper.toFileDto(value.getFile()), value.getAction()))
                .toList();
    }

    public UpdateEventResponse updateFromEntity(Event event) {
        return new UpdateEventResponse(event.getId(), UserMapper.toUserWithoutEventDto(event.getUser()),
                FileMapper.toFileDto(event.getFile()), event.getAction());
    }

    public Event deleteToEntity(DeleteEventRequest dto) {
        return Event.builder().id(dto.id()).build();
    }
}
