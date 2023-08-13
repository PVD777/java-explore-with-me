package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.dto.CommentDtoOut;
import ru.practicum.comments.model.dto.CommentDtoIn;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoOut create(@PathVariable Integer userId, @PathVariable Integer eventId,
                                @Valid @RequestBody CommentDtoIn commentDtoIn) {
        log.debug("Публикация комментария");
        return commentService.create(userId, eventId, commentDtoIn);
    }

    @GetMapping
    public List<CommentDtoOut> getAllByUserId(@PathVariable Integer userId,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение всех комментариев пользователя");
        return commentService.getAllByUserId(userId, PageRequest.of(from, size));
    }

    @GetMapping("/events/{eventId}")
    public CommentDtoOut getAllByUserAndEventId(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.debug("Получение всех комментариев пользователя по событию");
        return commentService.getAllByUserAndEventId(userId, eventId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId, @PathVariable Integer commentId) {
        log.debug("Удаление комментария");
        commentService.delete(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDtoOut update(@PathVariable Integer userId, @PathVariable Integer commentId,
                                @Valid @RequestBody CommentDtoIn commentDtoIn) {
        log.debug("Обновление комментария");
        return commentService.update(userId, commentId, commentDtoIn);
    }

}
