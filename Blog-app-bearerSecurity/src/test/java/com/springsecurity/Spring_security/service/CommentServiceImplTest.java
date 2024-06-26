package com.springsecurity.Spring_security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springsecurity.Spring_security.dto.CommentDto;
import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.entity.Comment;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.payload.CommentResponse;
import com.springsecurity.Spring_security.repository.CommentRepository;
import com.springsecurity.Spring_security.repository.PostRepository;
import com.springsecurity.Spring_security.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.springsecurity.Spring_security.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CommentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    private Category category;

    private Category category2;

    private User user;

    private User user2;

    private Post post;

    private Post post2;

    private Comment comment;

    @BeforeEach
    void setUp() throws Exception {
        category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1);
        category.setCategoryTitle("Dr");
        category.setPost(new ArrayList<>());

        category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1);
        category2.setCategoryTitle("Dr");
        category2.setPost(new ArrayList<>());

        user = new User();
        user.setAbout("About");
        user.setComments(new HashSet<>());
        user.setContact(1L);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setPost(new ArrayList<>());
        user.setRole(Role.USER);

        user2 = new User();
        user2.setAbout("About");
        user2.setComments(new HashSet<>());
        user2.setContact(1L);
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setPost(new ArrayList<>());
        user2.setRole(Role.USER);

        post = new Post();
        post.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post.setCategory(category);
        post.setComments(new HashSet<>());
        post.setContent("Not all who wander are lost");
        post.setImageName("Image Name");
        post.setPostId(1);
        post.setTitle("Dr");
        post.setUser(user);

        post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user2);

        comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setId(1);
        comment.setPost(post);
        comment.setUser(user2);

    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment() {

        when(commentRepository.save(Mockito.<Comment>any())).thenReturn(comment);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post2));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        CommentDto actualCreateCommentResult = commentServiceImpl.createComment(new CommentDto(), 1, 1);

        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
        assertNull(actualCreateCommentResult.getId());
        assertNull(actualCreateCommentResult.getContent());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment2() {
        when(commentRepository.save(Mockito.<Comment>any()))
                .thenThrow(new ResourceNotFoundException("creating comment: {} {} {}", "creating comment: {} {} {}", 42L));
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(new CommentDto(), 1, 1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment3() {
        when(postRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("creating comment: {} {} {}", "creating comment: {} {} {}", 42L));

        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        CommentDto commentDto = CommentDto.builder().content("Not all who wander are lost").id(1).build();

        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(commentDto, 1, 1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment() {

        doNothing().when(commentRepository).delete(Mockito.<Comment>any());
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(comment));

        commentServiceImpl.deleteComment(1);

        verify(commentRepository).delete(isA(Comment.class));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment2() {

        doThrow(new ResourceNotFoundException("deleting comment: {}", "deleting comment: {}", 42L)).when(commentRepository)
                .delete(Mockito.<Comment>any());
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(comment));

        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.deleteComment(1));
        verify(commentRepository).delete(isA(Comment.class));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment3() {
        Optional<Comment> emptyResult = Optional.empty();
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.deleteComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment() {

        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(comment));

        CommentDto actualComment = commentServiceImpl.getComment(1);

        verify(commentRepository).findById(eq(1));
        assertEquals("Not all who wander are lost", actualComment.getContent());
        assertEquals(1, actualComment.getId().intValue());
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment2() {
        // Arrange
        Optional<Comment> emptyResult = Optional.empty();
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment3() {
        // Arrange
        when(commentRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("getting comment: {}", "getting comment: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment() {
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(commentList);
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");

        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(0, actualAllComment.getPageSize());
        assertEquals(0L, actualAllComment.getTotalElement());
        assertEquals(1, actualAllComment.getTotalPages());
        assertTrue(actualAllComment.isLastPage());
        assertEquals(commentList, actualAllComment.getContent());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment2() {
        when(commentRepository.findAll()).thenThrow(
                new ResourceNotFoundException("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                        "Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", 42L));
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir"));
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment3() {

        Comment comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setId(1);
        comment.setPost(post);
        comment.setUser(user2);

        ArrayList<Comment> content = new ArrayList<>();
        content.add(comment);
        PageImpl<Comment> pageImpl = new PageImpl<>(content);
        when(commentRepository.findAll()).thenReturn(new ArrayList<>());
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");

        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        List<CommentDto> content2 = actualAllComment.getContent();
        assertEquals(1, content2.size());
        CommentDto getResult = content2.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(1, actualAllComment.getPageSize());
        assertEquals(1, actualAllComment.getTotalPages());
        assertEquals(1, getResult.getId().intValue());
        assertEquals(1L, actualAllComment.getTotalElement());
        assertTrue(actualAllComment.isLastPage());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment4() {

        Comment comment2 = new Comment();
        comment2.setContent("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        comment2.setId(2);
        comment2.setPost(post2);
        comment2.setUser(user2);

        ArrayList<Comment> content = new ArrayList<>();
        content.add(comment2);
        content.add(comment);
        PageImpl<Comment> pageImpl = new PageImpl<>(content);
        when(commentRepository.findAll()).thenReturn(new ArrayList<>());
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        List<CommentDto> content2 = actualAllComment.getContent();
        assertEquals(2, content2.size());
        CommentDto getResult = content2.get(0);
        assertEquals("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                getResult.getContent());
        CommentDto getResult2 = content2.get(1);
        assertEquals("Not all who wander are lost", getResult2.getContent());
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(1, actualAllComment.getTotalPages());
        assertEquals(1, getResult2.getId().intValue());
        assertEquals(2, actualAllComment.getPageSize());
        assertEquals(2, getResult.getId().intValue());
        assertEquals(2L, actualAllComment.getTotalElement());
        assertTrue(actualAllComment.isLastPage());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment5() {
        // Arrange
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(commentList);
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "asc");

        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(0, actualAllComment.getPageSize());
        assertEquals(0L, actualAllComment.getTotalElement());
        assertEquals(1, actualAllComment.getTotalPages());
        assertTrue(actualAllComment.isLastPage());
        assertEquals(commentList, actualAllComment.getContent());
    }

    /**
     * Method under test: {@link CommentServiceImpl#updateComment(int, CommentDto)}
     */
    @Test
    void testUpdateComment() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any())).thenReturn(comment);
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act
        CommentDto actualUpdateCommentResult = commentServiceImpl.updateComment(1, new CommentDto());

        // Assert
        verify(commentRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
        assertNull(actualUpdateCommentResult.getContent());
        assertEquals(1, actualUpdateCommentResult.getId().intValue());
    }

    /**
     * Method under test: {@link CommentServiceImpl#updateComment(int, CommentDto)}
     */
    @Test
    void testUpdateComment2() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any()))
                .thenThrow(new ResourceNotFoundException("updating comment: {} {}", "updating comment: {} {}", 42L));
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.updateComment(1, new CommentDto()));
        verify(commentRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
    }
}
