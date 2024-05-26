package com.finance.controller;

import com.finance.dto.CategoryDto;
import com.finance.model.Category;
import com.finance.security.UserPrincipal;
import com.finance.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDto categoryDto,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Category category = categoryService.saveCategory(categoryDto, userPrincipal);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Category> categories = categoryService.getAllCategories(userPrincipal);

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<Category> category = categoryService.getCategoryById(id, userPrincipal);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        categoryService.deleteCategory(id, userPrincipal);
        return ResponseEntity.ok().build();
    }
}