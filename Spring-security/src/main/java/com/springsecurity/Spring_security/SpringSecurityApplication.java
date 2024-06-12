package com.springsecurity.Spring_security;

import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<User> adminAccount = userRepository.findByRole(Role.ADMIN);
		for (User user1 : adminAccount) {
			if (user1 == null) {
				User user = new User();
				user.setEmail("admin@gmail.com");
				user.setFirstName("admin");
				user.setLastName("admin");
				user.setRole(Role.ADMIN);
				user.setPassword(new BCryptPasswordEncoder().encode("admin"));
				user.setContact(9989876765l);
				user.setAbout("I am admin");
				userRepository.save(user);
			}
		}
	}
}
