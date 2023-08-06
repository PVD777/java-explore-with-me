package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users/{userId}/requests")
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getByUserId(@PathVariable int userId) {
        log.debug("Получение информации о заявках текущего пользователя на участие в чужих событиях");
        return requestService.getByRequesterId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable int userId, @RequestParam int eventId) {
        log.debug("Добавление запроса от текущего пользователя на участие в событии");
        return  requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable int userId, @PathVariable int requestId) {
        log.debug("Отмена своего запроса на участие в событии");
        return requestService.cancel(userId, requestId);
    }
}
