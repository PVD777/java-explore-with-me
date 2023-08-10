package ru.practicum.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    int id;

    List<EventShortDto> events;

    Boolean pinned;

    String title;
}
