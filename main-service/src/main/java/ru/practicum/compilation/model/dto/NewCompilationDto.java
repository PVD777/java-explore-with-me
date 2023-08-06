package ru.practicum.compilation.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.compilation.model.dto.Validated.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {

    List<Integer> events;
    Boolean pinned = Boolean.FALSE;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 50, groups = {Create.class, Update.class})
    String title;
}
