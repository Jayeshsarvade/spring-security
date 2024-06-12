package com.springsecurity.Spring_security.repository;

import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
     Optional<User> findByEmail(String email);
     User findByRole(Role role);
}
