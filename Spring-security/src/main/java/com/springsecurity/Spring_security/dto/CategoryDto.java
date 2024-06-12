package com.springsecurity.Spring_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Integer categoryId;
    @NotBlank(message = "categoryTitle cannot be blank")
    @Size(min = 4, message = "minimum size of category title is: 4")
    private String categoryTitle;

    @NotBlank(message = "categoryDescription cannot be blank")
    @Size(min = 10, message = "minimum size of category title is: 10")
    private String categoryDescription;
}
