package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dao.CompilationRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ObjectNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        List<Event> events;
        if (compilationDto.getEvents() == null) {
            events = Collections.emptyList();
        } else {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        return CompilationMapper.compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(int compId, NewCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ObjectNotFoundException("compilation не найдена"));
        List<Event> events;
        if (compilationDto.getEvents() == null) {
            events = Collections.emptyList();
        } else {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        compilation.setEvents(events);
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null && !compilationDto.getTitle().isBlank()) {
            compilation.setTitle(compilationDto.getTitle());
        }
        return CompilationMapper.compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ObjectNotFoundException("compilation не найдена"));
        compilationRepository.deleteById(compilation.getId());
    }

    @Override
    public CompilationDto getById(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ObjectNotFoundException("compilation не найдена"));
        return CompilationMapper.compilationToDto(compilation);
    }

    @Override
    public List<CompilationDto> get(Boolean pinned, Pageable pageable) {
        List<Compilation> compilationList;
        if (pinned != null) {
            compilationList = compilationRepository.findByPinned(pinned, pageable);
        } else {
            compilationList = compilationRepository.findAll(pageable).getContent();
        }
        return compilationList.stream()
                .map(CompilationMapper::compilationToDto)
                .collect(Collectors.toList());
    }
}
