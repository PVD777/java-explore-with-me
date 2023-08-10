package ru.practicum.event.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> searchEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


    EventFullDto create(Integer userId, NewEventDto newEventDto);

    EventFullDto updateByAdmin(int eventId, UpdateEventRequest updatedEvent);

    List<EventShortDto> search(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                               int from, int size, HttpServletRequest request);

    EventFullDto getByEventId(int eventId, HttpServletRequest request);

    List<EventShortDto> getByUserId(int userId, Pageable pageable);

    EventFullDto getByUserIdAndEventId(int eventId, int initiatorId);

    EventFullDto updateByUser(int userId, int eventId, UpdateEventRequest updatedEvent);
}
