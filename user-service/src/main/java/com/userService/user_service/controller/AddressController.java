package com.userService.user_service.controller;

import com.userService.user_service.dto.AddressDto;

import com.userService.user_service.payload.AddressResponse;
import com.userService.user_service.payload.ApiResponse;
import com.userService.user_service.payload.AppConstants;
import com.userService.user_service.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address/api")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping("/userId/{userId}")
	public ResponseEntity<AddressDto> getAddressByUserId(@PathVariable("userId") int userId) {

		AddressDto addressByUserId = addressService.findAddressByUserId(userId);
		return new ResponseEntity<AddressDto>(addressByUserId, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AddressDto> getAddress(@PathVariable int id) {
		AddressDto address = addressService.getAddress(id);
		return new ResponseEntity<>(address, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int id) {
		AddressDto updateAddress = addressService.updateAddress(addressDto, id);
		return new ResponseEntity<AddressDto>(updateAddress, HttpStatus.OK);
	}

	@PostMapping("/userId/{userId}")
	public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int userId) {
		AddressDto address = addressService.createAddress(addressDto, userId);
		return new ResponseEntity<AddressDto>(address, HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<AddressResponse> getAllAddress(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		AddressResponse allAddress = addressService.getAllAddress(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<AddressResponse>(allAddress, HttpStatus.OK);
	}

	@DeleteMapping("/userId/{userId}")
	public ResponseEntity<ApiResponse> deleteAddressByUserId(@PathVariable int userId) {
		addressService.deleteAddressByUserId(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("address deleted with userId: " + userId, true),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable int id) {
		addressService.deleteAddress(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("record deleted", true), HttpStatus.OK);
	}

}
