package com.springsecurity.Spring_security.payload;

import com.springsecurity.Spring_security.dto.UserDto;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private List<UserDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean lastPage;

}
