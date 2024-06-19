package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsUser;
import com.springsecurity.Spring_security.payload.UserResponse;
import com.springsecurity.Spring_security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * This method is used to get a user by its id.
     *
     * @param userId The id of the user to be retrieved.
     * @return ResponseEntity<UserDto> containing the user data if found, else returns HTTP 404 status.
     * @throws IllegalArgumentException If the userId is not a positive integer.
     */
    @Operation(summary = "Get User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the User",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable int userId){
        UserDto user = userService.getUser(userId);
        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    }

    /**
     * This method retrieves all users from the database.
     * It supports pagination, sorting, and filtering based on parameters.
     *
     * @param pageNo The page number to retrieve. Default is 0.
     * @param pageSize The number of users per page. Default is 10.
     * @param sortBy The field to sort by. Default is 'id'.
     * @param sortDir The direction of sorting. Default is 'asc'.
     * @return ResponseEntity<UserResponse> containing the list of users and pagination information.
     * If no users are found, it returns HTTP 404 status.
     */
    @Operation(summary = "Get All Users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the Users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found") })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsUser.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsUser.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsUser.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsUser.SORT_DIR, required = false) String sortDir
    ){
        UserResponse userResponse = userService.getAllUser(pageNo, pageSize,sortBy,sortDir);
        return new ResponseEntity<UserResponse>(userResponse,HttpStatus.OK);
    }

    /**
     * This method retrieves all users from the database based on a given role.
     *
     * @param role The role to filter users by.
     * @return ResponseEntity<List<UserDto>> containing the list of users with the specified role.
     * If no users are found with the given role, it returns HTTP 404 status.
     */
    @Operation(summary = "Get All Users By Role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the Users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found") })
    @GetMapping("/userRole")
    public ResponseEntity<List<UserDto>> getAllUsersByRole(@RequestParam Role role){
        List<UserDto> allUsersByRole = userService.getAllUsersByRole(role);
        return new ResponseEntity<List<UserDto>>(allUsersByRole,HttpStatus.OK);
    }

    /**
     * This method is used to update a user in the system.
     *
     * @param signUpRequest The updated user data. This should be a valid SignUpRequest object.
     * @param userId The id of the user to be updated. This should be a positive integer.
     * @return ResponseEntity<SignUpRequest> containing the updated user data if successful, else returns HTTP 400 or 404 status.
     * @throws IllegalArgumentException If the userId is not a positive integer.
     */
    @Operation(summary = "Update User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Updated Successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpRequest> updateUser(@Valid @RequestBody SignUpRequest signUpRequest, @PathVariable Integer userId){
        SignUpRequest updatedUSer = userService.updateUser(signUpRequest, userId);
        return new ResponseEntity<>(updatedUSer,HttpStatus.OK);
    }

    /**
     * This method is used to delete a user from the system.
     *
     * @param userId The id of the user to be deleted. This should be a positive integer.
     * @return ResponseEntity<ApiResponse> containing a success message if the user is deleted successfully,
     * else returns HTTP 400 or 404 status.
     * @throws IllegalArgumentException If the userId is not a positive integer.
     */
    @Operation(summary = "Delete User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("user deleted Successfully...", true), HttpStatus.OK);
    }
}
