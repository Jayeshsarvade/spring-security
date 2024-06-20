package com.userService.user_service.controller;

import com.userService.user_service.dto.AddressDto;

import com.userService.user_service.entity.Address;
import com.userService.user_service.payload.AddressResponse;
import com.userService.user_service.payload.ApiResponse;
import com.userService.user_service.payload.AppConstants;
import com.userService.user_service.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/address/api")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Operation(summary = "Get Address By UserId")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the address",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "address not found",
					content = @Content) })
	@GetMapping("/userId/{userId}")
	public ResponseEntity<AddressDto> getAddressByUserId(@PathVariable("userId") int userId) {

		AddressDto addressByUserId = addressService.findAddressByUserId(userId);
		return new ResponseEntity<AddressDto>(addressByUserId, HttpStatus.OK);
	}

	@Operation(summary = "Get Address By AddressId")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the address",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "address not found",
					content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<AddressDto> getAddress(@PathVariable int id) {
		AddressDto address = addressService.getAddress(id);
		return new ResponseEntity<>(address, HttpStatus.OK);
	}

	@Operation(summary = "Update Address By Its Id")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address Updated Successfully",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found",
					content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int id) {
		AddressDto updateAddress = addressService.updateAddress(addressDto, id);
		return new ResponseEntity<AddressDto>(updateAddress, HttpStatus.OK);
	}

	@Operation(summary = "Create Address", description = "creating address based on userID")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address Created",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) })})
	@PostMapping("/userId/{userId}")
	public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int userId) {
		AddressDto address = addressService.createAddress(addressDto, userId);
		return new ResponseEntity<AddressDto>(address, HttpStatus.CREATED);
	}

	@Operation(summary = "Get All Address")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the address",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found") })
	@GetMapping("/")
	public ResponseEntity<AddressResponse> getAllAddress(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		AddressResponse allAddress = addressService.getAllAddress(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<AddressResponse>(allAddress, HttpStatus.OK);
	}

	@Operation(summary = "Delete Address By UserId")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address Deleted",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found",
					content = @Content) })
	@DeleteMapping("/userId/{userId}")
	public ResponseEntity<ApiResponse> deleteAddressByUserId(@PathVariable int userId) {
		addressService.deleteAddressByUserId(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("address deleted with userId: " + userId, true),
				HttpStatus.OK);
	}

	@Operation(summary = "Delete Address By ID")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address Deleted",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)) }),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found",
					content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable int id) {
		addressService.deleteAddress(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("record deleted", true), HttpStatus.OK);
	}

}
