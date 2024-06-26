package com.userService.user_service.repository;


import com.userService.user_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address,Integer> {
	
	@Query(nativeQuery = true,value = "SELECT ea.id,ea.lane1,ea.lane2,ea.city,ea.state,ea.zip,ea.user_id FROM public.address ea join public.users u on u.id = ea.user_id where ea.user_id=:userId")
	Address findAddressByUserId(@Param("userId") int userId);

	@Query("SELECT a FROM Address a WHERE a.userId = :userId")
	Address findByUserId(int userId);

}
