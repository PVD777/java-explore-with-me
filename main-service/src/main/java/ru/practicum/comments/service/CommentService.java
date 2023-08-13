package ru.practicum.comments.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.comments.model.dto.CommentDtoOut;
import ru.practicum.comments.model.dto.CommentDtoIn;

import java.util.List;

public interface CommentService {
    CommentDtoOut create(Integer userId, Integer eventId, CommentDtoIn commentDtoIn);

    List<CommentDtoOut> getAllByUserId(Integer userId, Pageable pageable);

    CommentDtoOut getAllByUserAndEventId(Integer userId, Integer eventId);

    void delete(Integer userId, Integer commentId);
    void deleteByAdmin(Integer commentId);

    CommentDtoOut update(Integer userId, Integer commentId, CommentDtoIn commentDtoIn);

    List<CommentDtoOut> getAllByUserIds(List<Integer> users, Pageable pageable);

    List<CommentDtoOut> getAllByEventIds(List<Integer> events, Pageable pageable);
}
