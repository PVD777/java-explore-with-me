package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;


    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                           @RequestParam(value = "states", required = false) List<EventState> states,
                                           @RequestParam(value = "categories", required = false) List<Integer> categories,
                                           @RequestParam(value = "rangeStart", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(value = "rangeEnd", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.debug("Поиск событий");
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));
    }


    @PatchMapping("{eventId}")
    public EventFullDto update(@PathVariable int eventId, @Valid @RequestBody UpdateEventRequest updatedEvent) {
        log.debug("Редактирование данных события и его статуса (отклонение/публикация)");
        return eventService.updateByAdmin(eventId, updatedEvent);
    }
}
