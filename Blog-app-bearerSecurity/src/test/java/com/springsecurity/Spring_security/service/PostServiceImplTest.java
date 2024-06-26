package com.springsecurity.Spring_security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springsecurity.Spring_security.dto.AddressDto;
import com.springsecurity.Spring_security.dto.CategoryDto;
import com.springsecurity.Spring_security.dto.PostDto;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Category;
import com.springsecurity.Spring_security.entity.Comment;
import com.springsecurity.Spring_security.entity.Post;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.openfeignclient.AddressClient;
import com.springsecurity.Spring_security.payload.PostResponse;
import com.springsecurity.Spring_security.repository.CategoryRepository;
import com.springsecurity.Spring_security.repository.PostRepository;
import com.springsecurity.Spring_security.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.springsecurity.Spring_security.service.impl.PostServiceImpl;
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

@ContextConfiguration(classes = {PostServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PostServiceImplTest {
    @MockBean
    private AddressClient addressClient;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl postServiceImpl;

    @MockBean
    private UserRepository userRepository;

    private Category category;

    private Category category2;

    private User user;

    private User user2;

    private UserDto userDto;

    private Post post;

    private Post post2;

    private AddressDto addressDto;

    private CategoryDto categoryDto;

    private Comment comment;

    private Comment comment2;

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

        categoryDto = CategoryDto.builder()
                .categoryDescription("Category Description")
                .categoryId(1)
                .categoryTitle("Dr")
                .build();

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

        userDto = UserDto.builder()
                .about("About")
                .contact(1L)
                .email("jane.doe@example.org")
                .firstName("Jane")
                .id(1)
                .lastName("Doe")
                .password("iloveyou")
                .role(Role.USER)
                .build();

        post = new Post();
        Date addDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        post.setAddDate(addDate);
        post.setCategory(category2);
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

        comment2 = new Comment();
        comment2.setContent("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        comment2.setId(2);
        comment2.setPost(post2);
        comment2.setUser(user2);

        addressDto = AddressDto.builder().city("Oxford").id(1).lane1("Lane1").lane2("Lane2").state("MD").zip(1).build();
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost() {
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        PostDto actualCreatePostResult = postServiceImpl.createPost(new PostDto(), 1, 1);

        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user3 = actualCreatePostResult.getUser();
        assertEquals("About", user3.getAbout());
        CategoryDto category3 = actualCreatePostResult.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Doe", user3.getLastName());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Dr", actualCreatePostResult.getTitle());
        assertEquals("Image Name", actualCreatePostResult.getImageName());
        assertEquals("Jane", user3.getFirstName());
        assertEquals("Not all who wander are lost", actualCreatePostResult.getContent());
        assertEquals("iloveyou", user3.getPassword());
        assertEquals("jane.doe@example.org", user3.getEmail());
        assertNull(user3.getAddressDto());
        assertNull(actualCreatePostResult.getComments());
        assertEquals(1, actualCreatePostResult.getPostId());
        assertEquals(1, category3.getCategoryId().intValue());
        assertEquals(1, user3.getId().intValue());
        assertEquals(1L, user3.getContact());
        assertEquals(Role.USER, user3.getRole());
        assertTrue(user3.isAccountNonExpired());
        assertSame(post.getAddDate(), actualCreatePostResult.getAddedDate());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost2() {

        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.save(Mockito.<Post>any()))
                .thenThrow(new ResourceNotFoundException("Creating post: {} {} {}", "Creating post: {} {} {}", 42L));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost() {
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post2);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        PostDto actualUpdatePostResult = postServiceImpl.updatePost(new PostDto(), 1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user4 = actualUpdatePostResult.getUser();
        assertEquals("About", user4.getAbout());
        CategoryDto category3 = actualUpdatePostResult.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Doe", user4.getLastName());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Jane", user4.getFirstName());
        assertEquals("Oxford", user4.getAddressDto().getCity());
        assertEquals("iloveyou", user4.getPassword());
        assertEquals("jane.doe@example.org", user4.getEmail());
        assertNull(actualUpdatePostResult.getContent());
        assertNull(actualUpdatePostResult.getImageName());
        assertNull(actualUpdatePostResult.getTitle());
        assertEquals(1, actualUpdatePostResult.getPostId());
        assertEquals(1, category3.getCategoryId().intValue());
        assertEquals(1, user4.getId().intValue());
        assertEquals(1L, user4.getContact());
        assertEquals(Role.USER, user4.getRole());
        assertTrue(user4.isAccountNonExpired());
        assertTrue(actualUpdatePostResult.getComments().isEmpty());
        assertSame(post.getAddDate(), actualUpdatePostResult.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt()))
                .thenThrow(new ResourceNotFoundException("Updating post: {} {}", "Updating post: {} {}", 42L));

        when(postRepository.save(Mockito.<Post>any())).thenReturn(post2);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.updatePost(new PostDto(), 1));
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost3() {

        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        when(userRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("Updating post: {} {}", "Updating post: {} {}", 42L));
        PostDto.PostDtoBuilder builderResult = PostDto.builder();
        PostDto.PostDtoBuilder categoryResult = builderResult.category(categoryDto);
        PostDto.PostDtoBuilder titleResult = categoryResult.comments(new HashSet<>())
                .content("Not all who wander are lost")
                .imageName("Image Name")
                .postId(1)
                .title("Dr");

        PostDto postDto = titleResult.user(userDto).build();

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.updatePost(postDto, 1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
    }

    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost() {

        doNothing().when(postRepository).delete(Mockito.<Post>any());
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));

        postServiceImpl.deletePost(1);

        verify(postRepository).delete(isA(Post.class));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost2() {

        doThrow(new ResourceNotFoundException("deleting post: {}", "deleting post: {}", 42L)).when(postRepository)
                .delete(Mockito.<Post>any());
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.deletePost(1));
        verify(postRepository).delete(isA(Post.class));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost3() {
        Optional<Post> emptyResult = Optional.empty();
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.deletePost(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));

        PostDto actualPostById = postServiceImpl.getPostById(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        UserDto user2 = actualPostById.getUser();
        assertEquals("About", user2.getAbout());
        CategoryDto category2 = actualPostById.getCategory();
        assertEquals("Category Description", category2.getCategoryDescription());
        assertEquals("Doe", user2.getLastName());
        assertEquals("Dr", category2.getCategoryTitle());
        assertEquals("Dr", actualPostById.getTitle());
        assertEquals("Image Name", actualPostById.getImageName());
        assertEquals("Jane", user2.getFirstName());
        assertEquals("Not all who wander are lost", actualPostById.getContent());
        assertEquals("Oxford", user2.getAddressDto().getCity());
        assertEquals("iloveyou", user2.getPassword());
        assertEquals("jane.doe@example.org", user2.getEmail());
        assertEquals(1, actualPostById.getPostId());
        assertEquals(1, category2.getCategoryId().intValue());
        assertEquals(1, user2.getId().intValue());
        assertEquals(1L, user2.getContact());
        assertEquals(Role.USER, user2.getRole());
        assertTrue(user2.isAccountNonExpired());
        assertTrue(actualPostById.getComments().isEmpty());
        assertSame(post.getAddDate(), actualPostById.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt()))
                .thenThrow(new ResourceNotFoundException("get post by Id: {}", "get post by Id: {}", 42L));
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostById(1));
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        Date addDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        post2.setAddDate(addDate);
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user2);
        Optional<Post> ofResult = Optional.of(post2);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        PostDto actualPostById = postServiceImpl.getPostById(1);

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        UserDto user4 = actualPostById.getUser();
        assertEquals("About", user4.getAbout());
        CategoryDto category3 = actualPostById.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Doe", user4.getLastName());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Dr", actualPostById.getTitle());
        assertEquals("Image Name", actualPostById.getImageName());
        assertEquals("Jane", user4.getFirstName());
        assertEquals("Not all who wander are lost", actualPostById.getContent());
        assertEquals("Oxford", user4.getAddressDto().getCity());
        assertEquals("iloveyou", user4.getPassword());
        assertEquals("jane.doe@example.org", user4.getEmail());
        assertEquals(1, actualPostById.getPostId());
        assertEquals(1, category3.getCategoryId().intValue());
        assertEquals(1, user4.getId().intValue());
        assertEquals(1, actualPostById.getComments().size());
        assertEquals(1L, user4.getContact());
        assertEquals(Role.USER, user4.getRole());
        assertTrue(user4.isAccountNonExpired());
        assertSame(addDate, actualPostById.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById4() {
        // Arrange
        Optional<Post> emptyResult = Optional.empty();
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostById(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost() {
        ArrayList<Post> content = new ArrayList<>();
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));

        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(0, actualAllPost.getPageSize());
        assertEquals(0L, actualAllPost.getTotalElement());
        assertEquals(1, actualAllPost.getTotalPages());
        assertTrue(actualAllPost.isLastPage());
        assertEquals(content, actualAllPost.getContent());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost2() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post2);
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(2, actualAllPost.getPageSize());
        assertEquals(2, actualAllPost.getContent().size());
        assertEquals(2L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post2);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost5() {

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post2);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost6() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        ArrayList<Post> content = new ArrayList<>();
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "asc");

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory() {

        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(new ArrayList<>());

        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);

        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertTrue(actualPostsByCategory.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory2() {

        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(Mockito.<Category>any()))
                .thenThrow(new ResourceNotFoundException("getting post by category: {}", "getting post by category: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByCategory(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory3() {
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByCategory(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);

        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory5() {
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);

        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);

        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(2, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory6() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);

        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory7() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);

        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser() {
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertTrue(actualPostsByUser.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser2() {
        when(postRepository.findByUser(Mockito.<User>any()))
                .thenThrow(new ResourceNotFoundException("getting post by user: {}", "getting post by user: {}", 42L));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByUser(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        verify(addressClient, atLeast(1)).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(2, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser5() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser6() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts() {
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(new ArrayList<>());

        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");

        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertTrue(actualSearchPostsResult.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts2() {
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);

        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts3() {
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);

        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");

        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(2, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        Comment comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setId(1);
        comment.setPost(post);
        comment.setUser(user2);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);

        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts5() {
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);

        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented() {
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(new ArrayList<>());

        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);

        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertTrue(actualUsersWhoCommented.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented2() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);

        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);

        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertEquals(1, actualUsersWhoCommented.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);

        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);

        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertEquals(2, actualUsersWhoCommented.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented4() {
        when(addressClient.getAddressByUserId(anyInt()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", 42L));
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);

        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getUsersWhoCommented(1));
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findUsersByCommentsPostId(eq(1));
    }
}
