package com.springsecurity.Spring_security.controller;


import com.springsecurity.Spring_security.dto.PostDto;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsPost;
import com.springsecurity.Spring_security.payload.PostResponse;
import com.springsecurity.Spring_security.service.FileService;
import com.springsecurity.Spring_security.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private FileService fileService;

    /**
     * This method is used to create a new post.
     *
     * @param postDto The post data to be created.
     * @param userId The id of the user who is creating the post.
     * @param categoryId The id of the category to which the post belongs.
     * @return A ResponseEntity containing the created post data and a HTTP status code of 201 (Created).
     * @throws IllegalArgumentException If the user id or category id is invalid.
     * @throws ChangeSetPersister.NotFoundException If the user id or category id is not found.
     */
    @Operation(summary = "Create Post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user id or category id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User id or category id not found",
                    content = @Content) })
    @PostMapping(value = "/user/{userId}/category/{categoryId}/posts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {
        PostDto post = postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<PostDto>(post, HttpStatus.CREATED);
    }

    /**
     * This method retrieves a list of posts created by a specific user.
     *
     * @param userId The unique identifier of the user whose posts are to be retrieved.
     * @return A ResponseEntity containing a list of PostDto objects representing the posts created by the user.
     *         The HTTP status code of the response is 200 (OK).
     * @throws IllegalArgumentException If the provided userId is invalid.
     * @throws ChangeSetPersister.NotFoundException If the user with the provided userId is not found.
     */
    @Operation(summary = "Get Posts By User")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the posts",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid userId supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @GetMapping(value = "/user/{userId}/posts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDto>> getPostByUser(
            @PathVariable Integer userId) {
        List<PostDto> postsByUser = postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDto>>(postsByUser, HttpStatus.OK);
    }

    /**
     * This method retrieves a list of posts created in a specific category.
     *
     * @param categoryId The unique identifier of the category whose posts are to be retrieved.
     * @return A ResponseEntity containing a list of PostDto objects representing the posts in the category.
     *         The HTTP status code of the response is 200 (OK).
     * @throws IllegalArgumentException If the provided categoryId is invalid.
     * @throws ChangeSetPersister.NotFoundException If the category with the provided categoryId is not found.
     */
    @Operation(summary = "Get Posts By Category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the posts",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid categoryId supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @GetMapping(value = "/category/{categoryId}/posts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDto>> getPostsByCategory(
            @PathVariable Integer categoryId) {
        List<PostDto> postsByCategory = postService.getPostsByCategory(categoryId);
        return new ResponseEntity<List<PostDto>>(postsByCategory, HttpStatus.OK);
    }

    /**
     * This method retrieves a post by its unique identifier.
     *
     * @param postId The unique identifier of the post to be retrieved.
     * @return A ResponseEntity containing the PostDto object representing the post.
     *         The HTTP status code of the response is 200 (OK).
     * @throws IllegalArgumentException If the provided postId is invalid.
     * @throws ChangeSetPersister.NotFoundException If the post with the provided postId is not found.
     */
    @Operation(summary = "Get Post By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the post",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @GetMapping(value = "/posts/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> getPost(@PathVariable Integer postId) {
        PostDto postById = postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
    }

    /**
     * This method retrieves all posts with pagination, sorting, and filtering options.
     *
     * @param pageNo The page number to retrieve. Default value is defined in AppConstantsPost.PAGE_NO.
     * @param pageSize The number of posts per page. Default value is defined in AppConstantsPost.PAGE_SIZE.
     * @param sortBy The field to sort the posts by. Default value is defined in AppConstantsPost.SORT_BY.
     * @param sortDir The direction to sort the posts. Default value is defined in AppConstantsPost.SORT_DIR.
     * @return A ResponseEntity containing a PostResponse object representing the paginated, sorted, and filtered posts.
     *         The HTTP status code of the response is 200 (OK).
     * @throws ChangeSetPersister.NotFoundException If no posts are found.
     */
    @Operation(summary = "Get All Posts")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the posts",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Posts not found",
                    content = @Content) })
    @GetMapping(value = "/posts/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsPost.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsPost.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsPost.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsPost.SORT_DIR, required = false) String sortDir
    ) {
        PostResponse allPost = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<PostResponse>(allPost, HttpStatus.OK);
    }

    /**
     * This method is used to delete a post by its unique identifier.
     *
     * @param postId The unique identifier of the post to be deleted.
     * @return A ResponseEntity containing an ApiResponse object indicating the success of the deletion.
     *         The HTTP status code of the response is 200 (OK).
     * @throws IllegalArgumentException If the provided postId is invalid.
     * @throws ChangeSetPersister.NotFoundException If the post with the provided postId is not found.
     */
    @Operation(summary = "Delete Post By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @DeleteMapping(value = "/posts/{postId}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Post Is Successfully Deleted", true), HttpStatus.OK);
    }

    /**
     * This method is used to update a post by its unique identifier.
     *
     * @param postDto The updated post data.
     * @param postId The unique identifier of the post to be updated.
     * @return A ResponseEntity containing the updated post data and a HTTP status code of 200 (OK).
     * @throws IllegalArgumentException If the provided postId is invalid.
     * @throws ChangeSetPersister.NotFoundException If the post with the provided postId is not found.
     */
    @Operation(summary = "Update post By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "post Updated Successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @PutMapping(value = "/posts/{postId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        PostDto update = postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDto>(update, HttpStatus.OK);
    }

    /**
     * This method is used to search for posts based on a keyword.
     *
     * @param keyword The keyword to search for in the post content.
     * @return A ResponseEntity containing a list of PostDto objects representing the posts that match the keyword.
     *         The HTTP status code of the response is 200 (OK).
     * @throws ChangeSetPersister.NotFoundException If no posts are found that match the keyword.
     */
    @Operation(summary = "Search post By Its keyword")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found post Successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content) })
    @GetMapping(value = "/posts/search/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDto>> searchPosts(@PathVariable String keyword) {
        List<PostDto> posts = postService.searchPosts(keyword);
        return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
    }

    /**
     * This method handles file uploads for posts.
     *
     * @param image The MultipartFile object representing the uploaded image.
     * @param postId The unique identifier of the post to which the image belongs.
     * @return A ResponseEntity containing the updated post data with the uploaded image name.
     * @throws IOException If an error occurs during file upload.
     */
    @Operation(summary = "File Upload")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "File uploaded successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid post id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "post id not found",
                    content = @Content) })
    @PostMapping(value = "/post/image/upload/{postId}")
    public ResponseEntity<PostDto> fileUpload(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId) throws IOException {

        PostDto postDto = postService.getPostById(postId);
        String fileName = fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = postService.updatePost(postDto, postId);

        return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
    }

    /**
     * This method retrieves a list of users who have commented on a specific post.
     *
     * @param postId The unique identifier of the post for which the users who commented are to be retrieved.
     * @return A ResponseEntity containing a list of UserDto objects representing the users who commented on the post.
     *         The HTTP status code of the response is 200 (OK).
     * @throws ChangeSetPersister.NotFoundException If the post with the provided postId is not found.
     */
    @Operation(summary = "Get Users Who Commented")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the User",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping(value = "post/postId/{postId}")
    public ResponseEntity<List<UserDto>> getUsersWhoCommented(@PathVariable int postId){
    	List<UserDto> usersWhoCommented = postService.getUsersWhoCommented(postId);
    	return new ResponseEntity<List<UserDto>>(usersWhoCommented, HttpStatus.OK);
    	
    }
}
