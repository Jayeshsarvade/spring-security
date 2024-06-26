package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.CommentDto;
import com.springsecurity.Spring_security.entity.Comment;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsComment;
import com.springsecurity.Spring_security.payload.CommentResponse;
import com.springsecurity.Spring_security.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * This method is used to create a new comment for a specific post.
     *
     * @param commentDto The comment data to be created.
     * @param userId The id of the user who is creating the comment.
     * @param postId The id of the post to which the comment is being added.
     * @return A ResponseEntity containing the created comment and a HTTP status code of 201 (Created).
     *
     * @throws IllegalArgumentException If the user id or post id is invalid.
     * @throws ChangeSetPersister.NotFoundException If the user id or post id is not found.
     */
    @Operation(summary = "Create Comment")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user id or post id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User id or post id not found",
                    content = @Content) })
    @PostMapping(value = "/user/{userId}/post/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Integer userId,
            @PathVariable Integer postId
    ){
        CommentDto comment = commentService.createComment(commentDto,userId, postId);
        return new ResponseEntity<CommentDto>(comment, HttpStatus.CREATED);
    }

    /**
     * This method is used to delete a comment by its id.
     *
     * @param commentId The id of the comment to be deleted.
     * @return A ResponseEntity containing an ApiResponse with a success message and a HTTP status code of 200 (OK).
     *
     * @throws IllegalArgumentException If the comment id is invalid.
     * @throws ChangeSetPersister.NotFoundException If the comment id is not found.
     */
    @Operation(summary = "Delete Comment By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content) })
    @DeleteMapping(value = "/{commentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Integer commentId
    ){
        commentService.deleteComment(commentId);
        return  new ResponseEntity<ApiResponse>(new ApiResponse("comment deleted Successfully...", true), HttpStatus.OK);
    }

    /**
     * This method is used to retrieve a comment by its id.
     *
     * @param commentId The id of the comment to be retrieved.
     * @return A ResponseEntity containing the requested comment and a HTTP status code of 200 (OK).
     *
     * @throws IllegalArgumentException If the comment id is invalid.
     * @throws ChangeSetPersister.NotFoundException If the comment id is not found.
     */
    @Operation(summary = "Get Comment By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the comment",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content) })
    @GetMapping(value = "/{commentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> getComment(@PathVariable Integer commentId){
        CommentDto comment = commentService.getComment(commentId);
        return new ResponseEntity<CommentDto>(comment, HttpStatus.OK);
    }

    /**
     * This method retrieves all comments from the database.
     * It supports pagination, sorting, and filtering.
     *
     * @param pageNo The page number to retrieve. Default is 0.
     * @param pageSize The number of comments per page. Default is 10.
     * @param sortBy The field to sort the comments by. Default is 'id'.
     * @param sortDir The direction of sorting. Default is 'asc'.
     * @return A ResponseEntity containing a CommentResponse object with the retrieved comments and pagination information.
     *         HTTP status code is 200 (OK) if comments are found, 404 (Not Found) if no comments are found.
     */
    @Operation(summary = "Get All Comments")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the comments",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "comment not found") })
    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> getAllComments(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsComment.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsComment.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsComment.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsComment.SORT_DIR,required = false) String sortDir
    ){
        CommentResponse allComment = commentService.getAllComment(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<CommentResponse>(allComment, HttpStatus.OK);
    }

    /**
     * This method is used to update a comment by its id.
     *
     * @param id The id of the comment to be updated.
     * @param commentDto The updated comment data.
     * @return A ResponseEntity containing the updated comment and a HTTP status code of 200 (OK).
     *
     * @throws IllegalArgumentException If the comment id is invalid.
     * @throws ChangeSetPersister.NotFoundException If the comment id is not found.
     */
    @Operation(summary = "update comments by its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "comment updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "comment not found") })
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable int id,@RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }
}
