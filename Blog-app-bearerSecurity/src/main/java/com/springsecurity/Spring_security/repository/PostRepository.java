package com.springsecurity.Spring_security.repository;

import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String title);


    @Query("SELECT DISTINCT c.user FROM Comment c WHERE c.post.id = :postId")
    List<User> findUsersByCommentsPostId(@Param("postId") int postId);
}
