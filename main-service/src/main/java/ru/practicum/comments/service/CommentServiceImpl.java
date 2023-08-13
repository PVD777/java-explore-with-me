package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dao.CommentRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentMapper;
import ru.practicum.comments.model.dto.CommentDtoOut;
import ru.practicum.comments.model.dto.CommentDtoIn;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.model.User;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDtoOut create(Integer userId, Integer eventId, CommentDtoIn commentDtoIn) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        Comment comment = CommentMapper.dtoToComment(commentDtoIn, user, event);
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDtoOut> getAllByUserId(Integer userId, Pageable pageable) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ObjectNotFoundException("User не существует");
        }
        List<Comment> commentList = commentRepository.findByAuthorId(userId, pageable);
        return commentList.stream()
                .map(CommentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoOut getAllByUserAndEventId(Integer userId, Integer eventId) {
        checkUserExist(userId);
        checkEventId(eventId);
        return CommentMapper.commentToDto(commentRepository.findByAuthorIdAndEventId(userId, eventId));
    }

    @Override
    public void delete(Integer userId, Integer commentId) {
        checkUserExist(userId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(userId, comment);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteByAdmin(Integer commentId) {
        if (commentRepository.findById(commentId).isPresent()) {
            commentRepository.deleteById(commentId);
        }
    }

    @Override
    public CommentDtoOut update(Integer userId, Integer commentId, CommentDtoIn commentDtoIn) {
        checkUserExist(userId);
        Comment comment = getCommentById(commentId);
        if (Duration.between(comment.getCreated(), LocalDateTime.now()).compareTo(Duration.ofHours(2)) > 0) {
            throw new ValidationException("No more time to edit comment");
        }
        validateCommentAuthor(userId, comment);
        comment.setText(commentDtoIn.getText());
        return CommentMapper.commentToDto(comment);
    }

    @Override
    public List<CommentDtoOut> getAllByUserIds(List<Integer> users, Pageable pageable) {
        List<Comment> commentList = commentRepository.findByAuthorIdIn(users, pageable);
        return commentList.stream()
                .map(CommentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDtoOut> getAllByEventIds(List<Integer> events, Pageable pageable) {
        List<Comment> commentList = commentRepository.findByEventIdIn(events, pageable);
        return commentList.stream()
                .map(CommentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    private void validateCommentAuthor(Integer userId, Comment comment) {
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ValidationException("Ошибка доступа");
        }
    }

    private Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment не найден"));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User не найден"));
    }

    private void checkUserExist(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ObjectNotFoundException("User не существует");
        }
    }

    private Event getEventById(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
    }

    private void checkEventId(Integer eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new ObjectNotFoundException("Event не существует");
        }
    }
}
