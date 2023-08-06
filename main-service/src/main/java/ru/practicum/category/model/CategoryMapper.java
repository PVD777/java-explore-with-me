package ru.practicum.category.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {

    public Category dtoToCategory(CategoryDto dto) {
        return new Category(dto.getId(), dto.getName());
    }

    public CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
