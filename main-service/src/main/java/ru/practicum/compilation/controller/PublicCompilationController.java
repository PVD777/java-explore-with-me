package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> get(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение подборок событий");
        return compilationService.get(pinned, PageRequest.of(from,size));
    }

    @GetMapping("{compId}")
    public CompilationDto getById(@PathVariable int compId) {
        log.debug("Получение подборки событий по его id");
        return compilationService.getById(compId);
    }
}
