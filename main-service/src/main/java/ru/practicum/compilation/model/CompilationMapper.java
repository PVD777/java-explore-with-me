package ru.practicum.compilation.model;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.model.dto.CompilationDto;

import ru.practicum.event.model.EventMapper;


import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public CompilationDto compilationToDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(
                compilation.getEvents()
                        .stream()
                        .map(EventMapper::eventToShortDto)
                        .collect(Collectors.toList())
        );
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}
