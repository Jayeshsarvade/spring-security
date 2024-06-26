package com.springsecurity.Spring_security.repository;

import com.springsecurity.Spring_security.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
