package ru.practicum.comments.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoOut {
    int id;
    String text;
    String authorName;
    @DateTimeFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
}
