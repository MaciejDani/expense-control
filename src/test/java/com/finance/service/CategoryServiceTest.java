package com.finance.service;

import com.finance.dto.CategoryDto;
import com.finance.exception.CategoryNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Category;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CategoryService categoryService;

    private UserPrincipal userPrincipal;
    private User user;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        user = new User();
        user.setId(1L);
        categoryDto = new CategoryDto();
        category = new Category();
    }

    @Test
    public void testSaveCategory() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(categoryDto, userPrincipal);

        assertNotNull(savedCategory);
        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testSaveCategory_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            categoryService.saveCategory(categoryDto, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testGetAllCategories() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByUser(user)).thenReturn(List.of(category));

        List<Category> categories = categoryService.getAllCategories(userPrincipal);

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository).findByUser(user);
    }

    @Test
    public void testGetAllCategories_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            categoryService.saveCategory(categoryDto, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testGetCategoryById() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategoryById(1L, userPrincipal);

        assertTrue(foundCategory.isPresent());
        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository).findByIdAndUser(1L, user);
    }

    @Test
    public void testGetCategoryById_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            categoryService.getCategoryById(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testDeleteCategory() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L, userPrincipal);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_CategoryNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(1L, userPrincipal);
        });

        verify(categoryRepository, never()).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            categoryService.deleteCategory(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository, never()).deleteById(anyLong());
    }


}
