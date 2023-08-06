package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;


import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        validate(categoryDto);
        Category savedCategory = categoryRepository.save(CategoryMapper.dtoToCategory(categoryDto));
        return CategoryMapper.categoryToDto(savedCategory);
    }

    @Override
    public CategoryDto get(int categoryId) {
        return CategoryMapper.categoryToDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found")));
    }

    @Override
    public CategoryDto update(int categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        if (!category.getName().equals(categoryDto.getName())) {
            validate(categoryDto);
            category.setName(categoryDto.getName());
        }
        return CategoryMapper.categoryToDto(category);
    }

    @Override
    public void delete(int categoryId) {
        if (eventRepository.findByCategoryId(categoryId).isPresent()) {
            throw new ValidationException("Категория занята");
        }
        categoryRepository.findById(categoryId).ifPresent(categoryRepository::delete);
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    private void validate(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ValidationException("category name conflict");
        }
    }
}
