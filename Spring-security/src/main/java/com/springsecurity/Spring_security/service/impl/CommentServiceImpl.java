package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.CommentDto;
import com.springsecurity.Spring_security.entity.Comment;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.payload.CommentResponse;
import com.springsecurity.Spring_security.repository.CommentRepository;
import com.springsecurity.Spring_security.repository.PostRepository;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * This method is used to create a new comment in the system.
	 *
	 * @param commentDto The comment data transfer object containing the content of the comment.
	 * @param userId The id of the user who is creating the comment.
	 * @param postId The id of the post to which the comment is being added.
	 * @return The created comment data transfer object with the id of the newly created comment.
	 * @throws ResourceNotFoundException If the user or post with the given ids does not exist.
	 */

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) {
		logger.info("creating comment: {} {} {}", commentDto, userId, postId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));

		Comment comment = new Comment();
		comment = Comment.builder().id(commentDto.getId()).content(commentDto.getContent()).build();
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment = commentRepository.save(comment);
		logger.info("created comment: {}", savedComment);
		return commentDto = CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}

	/**
	 * This method is used to delete a comment from the system.
	 *
	 * @param commentId The id of the comment to be deleted.
	 * @throws ResourceNotFoundException If a comment with the given id does not exist.
	 */

	@Override
	public void deleteComment(Integer commentId) {
		logger.info("deleting comment: {}", commentId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		logger.info("comment deleted: {}", comment);
		commentRepository.delete(comment);
	}

	/**
	 * This method retrieves a comment from the system based on the provided commentId.
	 *
	 * @param commentId The unique identifier of the comment to be retrieved.
	 * @return A CommentDto object containing the details of the retrieved comment.
	 * @throws ResourceNotFoundException If a comment with the given commentId does not exist.
	 */

	@Override
	public CommentDto getComment(Integer commentId) {
		CommentDto commentDto = null;
		logger.info("getting comment: {}", commentId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		logger.debug("got comment: {}", comment);
		return commentDto = CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}

	/**
	 * This method retrieves all comments from the system with pagination, sorting, and filtering.
	 *
	 * @param pageNo The page number to retrieve.
	 * @param pageSize The number of comments to retrieve per page.
	 * @param sortBy The field to sort the comments by.
	 * @param sortDir The direction to sort the comments (asc or desc).
	 * @return A CommentResponse object containing the details of the retrieved comments.
	 */

	@Override
	public CommentResponse getAllComment(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		logger.info("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
				sortBy, sortDir);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Comment> pagePost = commentRepository.findAll(pageable);
		List<Comment> content = pagePost.getContent();
		List<Comment> all = commentRepository.findAll();
		
		List<CommentDto> collect = content.stream().map(comment->CommentDto.builder().id(comment.getId()).content(comment.getContent()).build())
				.collect(Collectors.toList());
		
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setContent(collect);
		commentResponse.setPageNo(pagePost.getNumber());
		commentResponse.setPageSize(pagePost.getSize());
		commentResponse.setTotalElement(pagePost.getTotalElements());
		commentResponse.setTotalPages(pagePost.getTotalPages());
		commentResponse.setLastPage(pagePost.isLast());
		logger.debug("getting all comments: {}", commentResponse);
		return commentResponse;
	}

	/**
	 * This method is used to update an existing comment in the system.
	 *
	 * @param commentId The unique identifier of the comment to be updated.
	 * @param commentDto The comment data transfer object containing the updated content of the comment.
	 * @return A CommentDto object containing the details of the updated comment.
	 * @throws ResourceNotFoundException If a comment with the given commentId does not exist.
	 */

	@Override
	public CommentDto updateComment(int commentId, CommentDto commentDto) {
		logger.info("updating comment: {} {}", commentId, commentDto);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		comment.setContent(commentDto.getContent());
		Comment save = commentRepository.save(comment);
		logger.info("updated comment: {}", save);
		return CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}
}
