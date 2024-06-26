package com.springsecurity.Spring_security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

	private int postId;
	@NotEmpty(message = "title cannot be empty")
	private String title;
	@NotEmpty(message = "content should not be empty")
	private String content;
	private String imageName;

	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
	private Set<CommentDto> comments = new HashSet<>();
}
