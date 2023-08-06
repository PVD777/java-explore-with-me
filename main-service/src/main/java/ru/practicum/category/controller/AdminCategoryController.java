package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug("Добавление новой категории");
        return categoryService.create(categoryDto);
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int categoryId) {
        log.debug("Удаление категории");
        categoryService.delete(categoryId);
    }

    @PatchMapping("{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDto update(@PathVariable int categoryId, @Valid @RequestBody CategoryDto categoryDto) {
        log.debug("Изменение категории");
        return categoryService.update(categoryId, categoryDto);
    }
}
