package com.springsecurity.Spring_security.openfeignclient;

import com.springsecurity.Spring_security.dto.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//http://localhost:8080/address-app/address/152
@FeignClient(name = "Address-Service",url = "http://localhost:7070/address/api")
public interface AddressClient {

	@GetMapping("/userId/{id}")
	AddressDto getAddressByUserId(@PathVariable("id") int id);

	
	@DeleteMapping("/address/api/userId/{userId}")
	void deleteAddressByUserId(@PathVariable("userId") int userId);
	
}
