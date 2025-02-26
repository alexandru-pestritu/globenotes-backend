package com.app.globenotes_backend.service.category;

import com.app.globenotes_backend.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
}
