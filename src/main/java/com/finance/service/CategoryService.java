package com.finance.service;

import com.finance.dto.CategoryDto;
import com.finance.exception.CategoryNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.CategoryMapper;
import com.finance.model.Category;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public Category saveCategory(CategoryDto categoryDto, UserPrincipal userPrincipal) {
        Category category = CategoryMapper.fromDTO(categoryDto);

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);
        return categoryRepository.findByUser(user);
    }

    public Optional<Category> getCategoryById(Long id, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);
        return categoryRepository.findByIdAndUser(id, user);
    }

    public void deleteCategory(Long id, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                        .orElseThrow(UserNotFoundException::new);
        Optional<Category> category = categoryRepository.findByIdAndUser(id, user);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException();
        }
    }
}