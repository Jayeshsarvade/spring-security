package com.userService.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String lane1;
	private String lane2;
	private String city;
	private String state;
	private int zip;
	@Column(name = "user_id")
	private int userId;
}
