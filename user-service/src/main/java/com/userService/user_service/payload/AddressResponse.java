package com.userService.user_service.payload;

import com.userService.user_service.dto.AddressDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddressResponse {

    private List<AddressDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean lastPage;

}
