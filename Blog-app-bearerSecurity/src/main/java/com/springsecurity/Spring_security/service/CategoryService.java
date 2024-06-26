package com.springsecurity.Spring_security.service;

import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.payload.CategoryResponse;

public interface CategoryService {

    public CategoryDto createCategory(CategoryDto categoryDto);
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
    public void deleteCategory(Integer categoryId);
    public CategoryDto getCategory(Integer categoryId);
    public CategoryResponse getAllCategory(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
}
