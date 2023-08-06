package ru.practicum.event.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;



    @GetMapping
    public List<EventShortDto> search(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Integer> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) EventSort sort,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size,
            HttpServletRequest request
    ) {
        log.debug("Получение событий с возможностью фильтрации");
        return eventService.search(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("{eventId}")
    public EventFullDto getById(@PathVariable int eventId, HttpServletRequest request) {
        log.debug("Получение подробной информации об опубликованном событии по его идентификатору");
        return eventService.getByEventId(eventId, request);
    }
}
