package com.springsecurity.Spring_security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    @NotEmpty(message = "content should not empty")
    private String content;
}
