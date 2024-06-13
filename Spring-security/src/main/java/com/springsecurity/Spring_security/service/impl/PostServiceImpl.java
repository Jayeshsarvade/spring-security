package com.springsecurity.Spring_security.service.impl;


import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.dto.CommentDto;
import com.springsecurity.Spring_security.dto.PostDto;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.entity.Comment;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.payload.PostResponse;
import com.springsecurity.Spring_security.repository.CategoryRepository;
import com.springsecurity.Spring_security.repository.PostRepository;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    /**
     * This method creates a new post in the system.
     *
     * @param postDto    The DTO object containing the details of the post to be created.
     * @param userId     The ID of the user creating the post.
     * @param categoryId The ID of the category to which the post belongs.
     * @return A PostDto object containing the details of the newly created post.
     * @throws ResourceNotFoundException If the user or category with the specified ID does not exist.
     * @throws FeignException.NotFound   If the address associated with the user ID is not found.
     */

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        logger.info("Creating post: {} {} {}", postDto, userId, categoryId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "UserId", userId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

        CategoryDto categoryDto = CategoryDto.builder().categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle()).categoryDescription(category.getCategoryDescription())
                .build();

        UserDto userDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contact(user.getContact())
                .about(user.getAbout())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole()).build();

        Post post = Post.builder().postId(postDto.getPostId()).title(postDto.getTitle()).content(postDto.getContent())
                .addDate(postDto.getAddedDate()).imageName(postDto.getImageName()).category(category)
				.comments(null)
                .user(user).build();
        post.setImageName("def.png");
        post.setAddDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post savePost = postRepository.save(post);
        logger.info("created post: {}", savePost);
        return PostDto.builder().postId(savePost.getPostId()).title(savePost.getTitle()).content(savePost.getContent())
                .imageName(savePost.getImageName()).addedDate(savePost.getAddDate()).category(categoryDto).user(userDto)
                .build();
    }

    /**
     * This method updates an existing post in the system.
     *
     * @param postDto The DTO object containing the updated details of the post.
     * @param postId  The ID of the post to be updated.
     * @return A PostDto object containing the details of the updated post.
     * @throws ResourceNotFoundException If the post with the specified ID does not exist.
     * @throws FeignException.NotFound   If the address associated with the user ID is not found.
     */

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        logger.info("updating post: {} {}", postDto, postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
        Post save = postRepository.save(post);
        logger.info("post updated: {} ", save);


        Set<CommentDto> commentsList = new HashSet<>();
        for (Comment comment : post.getComments()) {
            commentsList.add(CommentDto.builder().id(comment.getId()).content(comment.getContent()).build());
        }

        CategoryDto categoryDto = CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                .categoryTitle(post.getCategory().getCategoryTitle())
                .categoryDescription(post.getCategory().getCategoryDescription()).build();

        UserDto userDto = UserDto.builder()
                .id(post.getUser().getId())
                .firstName(post.getUser().getFirstName())
                .lastName(post.getUser().getLastName())
                .contact(post.getUser().getContact())
                .email(post.getUser().getEmail())
                .password(post.getUser().getPassword())
                .about(post.getUser().getAbout())
                .role(post.getUser().getRole())
                .build();

        return PostDto.builder().postId(post.getPostId()).title(post.getTitle()).content(post.getContent())
                .imageName(post.getImageName()).addedDate(post.getAddDate()).category(categoryDto).user(userDto)
                .comments(commentsList).build();

    }

    /**
     * Deletes a post from the database.
     *
     * @param postId The unique identifier of the post to be deleted.
     * @throws ResourceNotFoundException If the post with the given postId does not exist in the database.
     */

    @Override
    public void deletePost(Integer postId) {
        logger.info("deleting post: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        postRepository.delete(post);
        logger.info("post deleted successfully...");
    }

    /**
     * Retrieves a post from the database based on the given postId.
     *
     * @param postId The unique identifier of the post to be retrieved.
     * @return The post object with the specified postId.
     * @throws ResourceNotFoundException If the post with the given postId does not exist in the database.
     */

    @Override
    public PostDto getPostById(Integer postId) {
        logger.info("get post by Id: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        logger.debug("post found: {}", post);

        Set<CommentDto> commentList = new HashSet<>();
        for (Comment comment : post.getComments()) {
            commentList.add(CommentDto.builder().id(comment.getId()).content(comment.getContent()).build());
        }

        CategoryDto categoryDto = CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                .categoryTitle(post.getCategory().getCategoryTitle())
                .categoryDescription(post.getCategory().getCategoryDescription()).build();

        UserDto userDto = UserDto.builder()
                .id(post.getUser().getId())
                .firstName(post.getUser().getFirstName())
                .lastName(post.getUser().getLastName())
                .email(post.getUser().getEmail())
                .password(post.getUser().getPassword())
                .about(post.getUser().getAbout())
                .role(post.getUser().getRole())
                .build();

        PostDto postDto = PostDto.builder().postId(post.getPostId()).title(post.getTitle()).content(post.getContent())
                .imageName(post.getImageName()).addedDate(post.getAddDate()).category(categoryDto).user(userDto)
                .comments(commentList).build();

        return postDto;
    }

    /**
     * This method retrieves a paginated list of all posts in the system.
     *
     * @param pageNo   The page number of the posts to retrieve.
     * @param pageSize The number of posts per page.
     * @param sortBy   The field by which to sort the posts.
     * @param sortDir  The direction of the sort, either "asc" for ascending or "desc" for descending.
     * @return A PostResponse object containing the list of PostDto objects and pagination details.
     * @throws IllegalArgumentException If the sortDir is not "asc" or "desc".
     * @throws FeignException.NotFound  If the address associated with the user ID is not found.
     */

    @Override
    public PostResponse getAllPost(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        logger.info("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
                sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pagePost = postRepository.findAll(pageable);
        List<Post> content = pagePost.getContent();

        List<PostDto> collect = content.stream()
                .map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
                        .content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
                        .category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                                .categoryTitle(post.getCategory().getCategoryTitle())
                                .categoryDescription(post.getCategory().getCategoryDescription()).build())
                        .user(UserDto.builder().id(post.getUser().getId())
                                .firstName(post.getUser().getFirstName())
                                .lastName(post.getUser().getLastName())
                                .contact(post.getUser().getContact())
                                .email(post.getUser().getEmail())
                                .password(post.getUser().getPassword())
                                .about(post.getUser().getAbout())
                                .role(post.getUser().getRole())
                                .build())
                        .comments(
                                post.getComments().stream()
                                        .map(comment -> CommentDto.builder().id(comment.getId())
                                                .content(comment.getContent()).build())
                                        .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        List<PostDto> postDtoList = new ArrayList<>();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(collect);
        postResponse.setPageNo(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElement(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());
        logger.info("fetched all post response: {}", postResponse);
        return postResponse;
    }

    /**
     * This method retrieves a list of posts belonging to a specific category.
     *
     * @param categoryId The ID of the category for which to retrieve posts.
     * @return A list of PostDto objects containing the details of the posts in the specified category.
     * @throws ResourceNotFoundException If the category with the specified ID does not exist.
     * @throws FeignException.NotFound   If the address associated with a user ID is not found.
     */

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        logger.info("getting post by category: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        List<Post> byCategory = postRepository.findByCategory(category);

        List<PostDto> collect = byCategory.stream()
                .map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
                        .content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
                        .category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                                .categoryTitle(post.getCategory().getCategoryTitle())
                                .categoryDescription(post.getCategory().getCategoryDescription()).build())
                        .user(UserDto.builder().id(post.getUser().getId())
                                .firstName(post.getUser().getFirstName())
                                .lastName(post.getUser().getLastName())
                                .contact(post.getUser().getContact())
                                .email(post.getUser().getEmail())
                                .password(post.getUser().getPassword())
                                .about(post.getUser().getAbout())
                                .role(post.getUser().getRole()).build())
                        .comments(
                                post.getComments().stream()
                                        .map(comment -> CommentDto.builder().id(comment.getId())
                                                .content(comment.getContent()).build())
                                        .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        List<PostDto> postDtoList = new ArrayList<>();
        logger.info("got post by category: {}", collect);
        return collect;
    }

    /**
     * This method retrieves a list of posts created by a specific user.
     *
     * @param userId The ID of the user for which to retrieve posts.
     * @return A list of PostDto objects containing the details of the posts created by the specified user.
     * @throws ResourceNotFoundException If the user with the specified ID does not exist.
     * @throws FeignException.NotFound   If the address associated with the user ID is not found.
     */

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        logger.info("getting post by user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
        List<Post> byUser = postRepository.findByUser(user);

        List<PostDto> collect = byUser.stream()
                .map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
                        .content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
                        .category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                                .categoryTitle(post.getCategory().getCategoryTitle())
                                .categoryDescription(post.getCategory().getCategoryDescription()).build())
                        .user(UserDto.builder().id(post.getUser().getId())
                                .firstName(post.getUser().getFirstName())
                                .lastName(post.getUser().getLastName())
                                .contact(post.getUser().getContact())
                                .email(post.getUser().getEmail())
                                .password(post.getUser().getPassword())
                                .role(post.getUser().getRole())
                                .about(post.getUser().getAbout()).build())
                        .comments(
                                post.getComments().stream()
                                        .map(comment -> CommentDto.builder().id(comment.getId())
                                                .content(comment.getContent()).build())
                                        .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        List<PostDto> postDtoList = new ArrayList<>();

        logger.info("got post by user: {}", collect);
        return collect;
    }

    /**
     * Searches for posts that contain the given keyword in their titles.
     *
     * @param keyword The keyword to search for in post titles.
     * @return A list of PostDto objects that match the search criteria.
     */

    @Override
    public List<PostDto> searchPosts(String keyword) {
        logger.info("searching posts: {}", keyword);
        List<Post> posts = postRepository.findByTitleContaining(keyword);

        List<PostDto> collect = posts.stream()
                .map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
                        .content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
                        .category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
                                .categoryTitle(post.getCategory().getCategoryTitle())
                                .categoryDescription(post.getCategory().getCategoryDescription()).build())
                        .user(UserDto.builder().id(post.getUser().getId())
                                .firstName(post.getUser().getFirstName())
                                .lastName(post.getUser().getLastName())
                                .contact(post.getUser().getContact())
                                .email(post.getUser().getEmail())
                                .password(post.getUser().getPassword())
                                .about(post.getUser().getAbout())
                                .role(post.getUser().getRole())
                                .build())
                                .comments(post.getComments().stream()
                                        .map(comment -> CommentDto.builder().id(comment.getId())
                                                .content(comment.getContent()).build())
                                        .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        logger.info("post searchred: {}", collect);

        List<PostDto> postDtoList = new ArrayList<>();

        return collect;
    }

    /**
     * Retrieves a list of users who have commented on a specific post.
     *
     * @param postId The unique identifier of the post.
     * @return A list of UserDto objects representing the users who have commented on the post.
     */

    @Override
    public List<UserDto> getUsersWhoCommented(int postId) {
        List<User> userByCommentPostId = postRepository.findUsersByCommentsPostId(postId);

        List<UserDto> collect = userByCommentPostId.stream()
                .map(user -> UserDto.builder().id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .about(user.getAbout())
                        .role(user.getRole())
                        .build())
                .toList();

        List<UserDto> userDtoList = new ArrayList<>();

        return collect;
    }
}
