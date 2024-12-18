package org.example.brief.application.service;

import org.example.brief.application.dto.request.CategoryRequest;
import org.example.brief.application.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponse> findAll(Pageable pageable);
    CategoryResponse create(CategoryRequest categoryDto);
    CategoryResponse update(CategoryRequest categoryDto ,Long id);
    void delete(Long id);
    CategoryResponse searchByName(String name);
}