package com.springsecurity.Spring_security.repository;

import com.springsecurity.Spring_security.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
