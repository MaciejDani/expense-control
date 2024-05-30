package com.finance.controller;

import com.finance.dto.CategoryDto;
import com.finance.exception.CategoryNotFoundException;
import com.finance.model.Category;
import com.finance.security.UserPrincipal;
import com.finance.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private UserPrincipal userPrincipal;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        categoryDto = new CategoryDto();
        category = new Category();
    }

    @Test
    public void testAddCategory() {
        when(categoryService.saveCategory(any(CategoryDto.class), any(UserPrincipal.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.addCategory(categoryDto, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService).saveCategory(any(CategoryDto.class), any(UserPrincipal.class));
    }

    @Test
    public void testGetAllCategories() {
        when(categoryService.getAllCategories(any(UserPrincipal.class))).thenReturn(List.of(category));

        ResponseEntity<List<Category>> response = categoryController.getAllCategories(userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(categoryService).getAllCategories(any(UserPrincipal.class));
    }

    @Test
    public void testGetCategoryById_Found() {
        when(categoryService.getCategoryById(anyLong(), any(UserPrincipal.class))).thenReturn(Optional.of(category));

        ResponseEntity<Category> response = categoryController.getCategoryById(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService).getCategoryById(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryService.getCategoryById(anyLong(), any(UserPrincipal.class))).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.getCategoryById(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoryService).getCategoryById(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(anyLong(), any(UserPrincipal.class));

        ResponseEntity<Void> response = categoryController.deleteCategory(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService).deleteCategory(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testDeleteCategory_NotFound() {
        doThrow(new CategoryNotFoundException()).when(categoryService).deleteCategory(anyLong(), any(UserPrincipal.class));

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryController.deleteCategory(1L, userPrincipal);
        });

        verify(categoryService).deleteCategory(anyLong(), any(UserPrincipal.class));
    }

}
