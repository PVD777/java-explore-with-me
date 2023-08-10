package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto get(int categoryId);

    CategoryDto update(int categoryId, CategoryDto categoryDto);

    void delete(int categoryId);

    List<CategoryDto> getAll(Pageable pageable);
}
