package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.UpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable int userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("Добавление нового события");
        return eventService.create(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getByUserId(@PathVariable int userId,
                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.debug("Получение событий, добавленных текущим пользователем");
        return eventService.getByUserId(userId, PageRequest.of(from, size));
    }

    @GetMapping("{eventId}")
    public EventFullDto getByUserIdAndEventId(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Получение полной информации о событии добавленном текущим пользователем");
        return eventService.getByUserIdAndEventId(eventId, userId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto update(@PathVariable int userId, @PathVariable int eventId,
                               @Valid @RequestBody UpdateEventRequest updatedEvent) {
        log.debug("Изменение события добавленного текущим пользователем");
        return eventService.updateByUser(userId, eventId, updatedEvent);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Получение информации о запросах на участие в событии текущего пользователя");
        return requestService.getByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable int userId, @PathVariable int eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest updatedRequest) {
        log.debug("Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя");
        return requestService.updateRequests(userId, eventId, updatedRequest);
    }
}
