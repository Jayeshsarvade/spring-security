package com.userService.user_service.service;

import com.userService.user_service.Exception.ResourceNotFoundException;
import com.userService.user_service.dto.AddressDto;
import com.userService.user_service.entity.Address;
import com.userService.user_service.feignclient.UserClient;
import com.userService.user_service.payload.AddressResponse;
import com.userService.user_service.repository.AddressRepository;

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
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserClient userClient;

    public AddressDto findAddressByUserId(int userId) {
        logger.info("Finding address by userId: {}", userId);
        Address address = addressRepository.findAddressByUserId(userId);
        if (address == null) {
            throw new ResourceNotFoundException("Address", "UserId", userId);
        }

        logger.debug("Address found: {}", address);
        return AddressDto.builder().id(address.getId()).lane1(address.getLane1()).lane2(address.getLane2())
                .city(address.getCity()).state(address.getState()).zip(address.getZip())
                .build();
    }

    public AddressDto getAddress(int id) {
        logger.info("Finding address by Id: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", id));

        logger.debug("Address found: {}", address);

        return AddressDto.builder().id(address.getId()).lane1(address.getLane1())
                .lane2(address.getLane2()).city(address.getCity()).state(address.getState())
                .zip(address.getZip())
                .build();
    }

    public AddressDto updateAddress(AddressDto addressDto, int id) {
        logger.debug("Updating address with id: {} {}", addressDto, id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", id));
        address.setLane1(addressDto.getLane1());
        address.setLane2(addressDto.getLane2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        Address save = addressRepository.save(address);
        logger.info("Address updated successfully: {}", save);
        return AddressDto.builder().id(address.getId()).lane1(address.getLane1()).lane2(address.getLane2())
                .city(address.getCity()).state(address.getState())
                .zip(address.getZip())
                .build();
    }

    public AddressDto createAddress(AddressDto addressDto, int userId) {

        Address address = Address.builder().id(addressDto.getId()).lane1(addressDto.getLane1())
                .lane2(addressDto.getLane2()).city(addressDto.getCity())
                .state(addressDto.getState())
                .zip(addressDto.getZip())
                .userId(userId).build();

        Address save = addressRepository.save(address);
        return AddressDto.builder().id(save.getId()).lane1(save.getLane1()).lane2(save.getLane2())
                .city(save.getCity()).state(save.getState())
                .zip(address.getZip())
                .build();
    }

    public AddressResponse getAllAddress(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        logger.info("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
                sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Address> pageAddress = addressRepository.findAll(pageable);
        List<Address> content = pageAddress.getContent();

        List<AddressDto> collect = content.stream().map(address->
                AddressDto.builder().id(address.getId()).lane1(address.getLane1())
                        .lane2(address.getLane2()).city(address.getCity())
                        .state(address.getState()).zip(address.getZip()).build()).collect(Collectors.toList());


        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(collect);
        addressResponse.setPageNo(pageAddress.getNumber());
        addressResponse.setPageSize(pageAddress.getSize());
        addressResponse.setTotalPages(pageAddress.getTotalPages());
        addressResponse.setTotalElement(pageAddress.getTotalElements());
        addressResponse.setLastPage(pageAddress.isLast());

        return addressResponse;
    }

    public void deleteAddressByUserId(int userId) {
        logger.debug("Deleting address by userId: {}", userId);
        Address address = addressRepository.findByUserId(userId);
        if (address!=null) {
            addressRepository.delete(address);
        }else {
            throw new ResourceNotFoundException("user","userId", userId);
        }
        logger.info("Address Deleted successfully....");
    }

    public void deleteAddress(int id) {
        logger.debug("Deleting address by id: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", id));
        addressRepository.delete(address);
        logger.info("Address Deleted successfully....");
    }

}
