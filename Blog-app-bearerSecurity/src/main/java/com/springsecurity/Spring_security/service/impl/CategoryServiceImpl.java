package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.payload.CategoryResponse;
import com.springsecurity.Spring_security.repository.CategoryRepository;
import com.springsecurity.Spring_security.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * This method is used to create a new category in the system.
     *
     * @param categoryDto The DTO object containing the details of the category to be created.
     * @return The DTO object of the newly created category.
     * @throws IllegalArgumentException If the input DTO object is null.
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        logger.info("Creating category: {}", categoryDto);
        Category category = new Category();
        category = Category.builder()
                .categoryId(categoryDto.getCategoryId())
                .categoryTitle(categoryDto.getCategoryTitle())
                .categoryDescription(categoryDto.getCategoryDescription()).build();
        Category saveCat = categoryRepository.save(category);
        logger.info("Created category: {}", saveCat);
        return categoryDto = CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .categoryDescription(category.getCategoryDescription()).build();
    }

    /**
     * This method is used to update an existing category in the system.
     *
     * @param categoryDto The DTO object containing the updated details of the category.
     * @param categoryId The unique identifier of the category to be updated.
     * @return The DTO object of the updated category.
     * @throws ResourceNotFoundException If the category with the given categoryId does not exist.
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        logger.info("Updating category with Id {}: {}", categoryDto, categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        Category savedCat = categoryRepository.save(category);
        logger.info("Updated user : {}", savedCat);
        return categoryDto = CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .categoryDescription(category.getCategoryDescription()).build();
    }

    /**
     * This method is used to delete an existing category in the system.
     *
     * @param categoryId The unique identifier of the category to be deleted.
     * @throws ResourceNotFoundException If the category with the given categoryId does not exist.
     */
    @Override
    public void deleteCategory(Integer categoryId) {
        logger.info("deleting category with Id: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        categoryRepository.delete(category);
        logger.info("Category deleted successfully....");
    }

    /**
     * This method retrieves a specific category from the system based on the provided categoryId.
     *
     * @param categoryId The unique identifier of the category to be retrieved.
     * @return The DTO object of the requested category.
     * @throws ResourceNotFoundException If the category with the given categoryId does not exist.
     */
    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        CategoryDto categoryDto = new CategoryDto();
        return categoryDto = CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .categoryDescription(category.getCategoryDescription()).build();
    }

    /**
     * This method retrieves all categories from the system with pagination, sorting, and filtering.
     *
     * @param pageNo The page number to retrieve.
     * @param pageSize The number of categories to retrieve per page.
     * @param sortBy The field to sort the categories by.
     * @param sortDir The direction to sort the categories (asc or desc).
     * @return A CategoryResponse object containing the details of the retrieved categories.
     */
    @Override
    public CategoryResponse getAllCategory(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        logger.info("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> pagePost = categoryRepository.findAll(pageable);
        List<Category> content = pagePost.getContent();

        List<CategoryDto> collect = content.stream().map(cat->CategoryDto.builder()
                .categoryId(cat.getCategoryId())
                .categoryTitle(cat.getCategoryTitle())
                .categoryDescription(cat.getCategoryDescription())
                .build()).collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(collect);
        categoryResponse.setPageNo(pagePost.getNumber());
        categoryResponse.setPageSize(pagePost.getSize());
        categoryResponse.setTotalElement(pagePost.getTotalElements());
        categoryResponse.setTotalPages(pagePost.getTotalPages());
        categoryResponse.setLastPage(pagePost.isLast());
        logger.info("Returning category response: {}",categoryResponse.toString());
        return categoryResponse;
    }
}
