package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.dto.CommentDtoOut;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/users")
    public List<CommentDtoOut> getAllByUserIds(@RequestParam(value = "users", required = false) List<Integer> users,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение комментариев пользователей");
        return commentService.getAllByUserIds(users, PageRequest.of(from, size));
    }

    @GetMapping("/events")
    public List<CommentDtoOut> getAllByEventIds(@RequestParam(value = "events", required = false) List<Integer> events,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение комментариев по событиям");
        return commentService.getAllByEventIds(events, PageRequest.of(from, size));
    }

    @DeleteMapping("/admin/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer commentId) {
        log.debug("Удаление комментария Администратором");
        commentService.deleteByAdmin(commentId);
    }
}
