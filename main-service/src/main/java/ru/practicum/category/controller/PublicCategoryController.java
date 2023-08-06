package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping("{categoryId}")
    public CategoryDto get(@PathVariable int categoryId) {
        log.debug("Получение категорий");
        return categoryService.get(categoryId);
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.debug("Получение информации о категории по её идентификатору");
        return categoryService.getAll(PageRequest.of(from, size));
    }
}
