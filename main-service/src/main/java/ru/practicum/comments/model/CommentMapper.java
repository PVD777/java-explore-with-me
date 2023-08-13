package ru.practicum.comments.model;

import lombok.experimental.UtilityClass;

import ru.practicum.comments.model.dto.CommentDtoOut;
import ru.practicum.comments.model.dto.CommentDtoIn;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment dtoToComment(CommentDtoIn commentDtoIn, User user, Event event) {
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public CommentDtoOut commentToDto(Comment comment) {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        if (comment == null) return commentDtoOut;
        commentDtoOut.setId(comment.getId());
        commentDtoOut.setAuthorName(comment.getAuthor().getName());
        commentDtoOut.setText(comment.getText());
        commentDtoOut.setCreated(comment.getCreated());
        return commentDtoOut;
    }
}
