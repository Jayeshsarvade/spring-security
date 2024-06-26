package com.userService.user_service.feignclient;

import com.userService.user_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//http://localhost:9090/api/users/202
@FeignClient(name = "User-Service", url = "http://localhost:8080/api/v1/user/")
public interface UserClient {

	@GetMapping("/{userId}")
	UserDto getAddressById(@PathVariable int userId);
	
}
