package org.example.brief.application.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.brief.application.dto.request.CategoryRequest;
import org.example.brief.application.dto.response.CategoryResponse;
import org.example.brief.application.mapper.CategoryMapper;
import org.example.brief.application.service.CategoryService;
import org.example.brief.domain.model.Category;
import org.example.brief.infrastructure.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryApplicationService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryDto, Long id) {
        Category exitsCategory = categoryRepository.findById(id).orElse(null);
        categoryMapper.udpateEntity(categoryDto, exitsCategory);
        Category savedCategory = categoryRepository.save(exitsCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }


    @Override
    public CategoryResponse searchByName(String name) {
        Category category = categoryRepository.findByName(name);
        return categoryMapper.toResponse(category);
    }
}
