package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto extends EventShortDto {
    String description;
    int participantLimit;
    EventState state;
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    Location location;
    boolean requestModeration;
}


