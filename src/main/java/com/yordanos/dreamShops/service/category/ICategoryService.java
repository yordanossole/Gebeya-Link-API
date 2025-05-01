package com.yordanos.dreamShops.service.category;

import com.yordanos.dreamShops.dto.CategoryDto;
import com.yordanos.dreamShops.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
}
