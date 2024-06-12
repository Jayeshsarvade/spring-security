package com.springsecurity.Spring_security.payload;

import com.springsecurity.Spring_security.dto.CategoryDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private List<CategoryDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean lastPage;
}
