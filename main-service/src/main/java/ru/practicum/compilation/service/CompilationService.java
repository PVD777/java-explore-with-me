package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compilationDto);

    CompilationDto update(int compId, NewCompilationDto compilationDto);

    void delete(int compId);

    CompilationDto getById(int compId);

    List<CompilationDto> get(Boolean pinned, Pageable pageable);
}
