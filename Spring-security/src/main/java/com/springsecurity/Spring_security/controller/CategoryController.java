package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsCategory;
import com.springsecurity.Spring_security.payload.CategoryResponse;
import com.springsecurity.Spring_security.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * This method is used to create a new category.
     *
     * @param categoryDto The category data to be created.
     * @return A ResponseEntity containing the created category data and a HTTP status code of 201 (Created).
     *
     * @throws IllegalArgumentException If the category data is invalid.
     */
    @Operation(summary = "Create Category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "category created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) })})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> crateCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }

    /**
     * This method is used to update an existing category by its id.
     *
     * @param categoryDto The updated category data.
     * @param categoryId The id of the category to be updated.
     * @return A ResponseEntity containing the updated category data and a HTTP status code of 200 (OK).
     *
     * @throws IllegalArgumentException If the category data is invalid or the category id is not found.
     */
    @Operation(summary = "Update Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "category updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @PutMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer categoryId
    ){
        CategoryDto updateCat = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<CategoryDto>(updateCat,HttpStatus.OK);
    }

    /**
     * This method is used to delete an existing category by its id.
     *
     * @param categoryId The id of the category to be deleted.
     * @return A ResponseEntity containing an ApiResponse with a success message and a HTTP status code of 200 (OK).
     *
     * @throws IllegalArgumentException If the category id is not found.
     */

    @Operation(summary = "Delete Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @DeleteMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted",true),HttpStatus.OK);
    }

    /**
     * This method is used to retrieve a category by its id.
     *
     * @param categoryId The id of the category to be retrieved.
     * @return A ResponseEntity containing the requested category data and a HTTP status code of 200 (OK).
     *         If the category id is not found, a ResponseEntity with a HTTP status code of 404 (Not Found) is returned.
     *         If the category id is invalid, a ResponseEntity with a HTTP status code of 400 (Bad Request) is returned.
     *
     * @throws IllegalArgumentException If the category id is not found or invalid.
     */
    @Operation(summary = "Get Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the category",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @GetMapping(value = "/{categoryId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
        CategoryDto category = categoryService.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
    }

    /**
     * This method retrieves all categories from the database.
     * It supports pagination, sorting, and filtering.
     *
     * @param pageNo The page number to retrieve. Default is 0.
     * @param pageSize The number of categories to retrieve per page. Default is 10.
     * @param sortBy The field to sort the categories by. Default is 'id'.
     * @param sortDir The direction to sort the categories. Default is 'asc'.
     * @return A ResponseEntity containing a CategoryResponse object with the retrieved categories and pagination information.
     *         If no categories are found, a ResponseEntity with a HTTP status code of 404 (Not Found) is returned.
     *
     * @throws IllegalArgumentException If the page number, page size, sort field, or sort direction is invalid.
     */
    @Operation(summary = "Get All Categories")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the categories",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categories not found")
    })
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
