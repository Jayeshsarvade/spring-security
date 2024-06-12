package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsCategory;
import com.springsecurity.Spring_security.payload.CategoryResponse;
import com.springsecurity.Spring_security.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> crateCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer categoryId
    ){
        CategoryDto updateCat = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<CategoryDto>(updateCat,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted",true),HttpStatus.OK);
    }

    @GetMapping(value = "/{categoryId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
        CategoryDto category = categoryService.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsCategory.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsCategory.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsCategory.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsCategory.SORT_DIR, required = false) String sortDir
    ){
        CategoryResponse allCategory = categoryService.getAllCategory(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<CategoryResponse>(allCategory,HttpStatus.OK);
    }
}
