package org.example.brief.presentation.rest;


import lombok.RequiredArgsConstructor;
import org.example.brief.application.dto.request.CategoryRequest;
import org.example.brief.application.dto.response.CategoryResponse;
import org.example.brief.application.service.CategoryService;
import org.example.brief.application.service.impl.CategoryApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        Page<CategoryResponse> categories = categoryService.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
      @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
             @RequestBody CategoryRequest categoryDto
    ) {
        CategoryResponse createdCategory = categoryService.create(categoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCategory);
    }

   @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest categoryDto,@PathVariable("id") Long id ){
         return ResponseEntity.ok(categoryService.update(categoryDto , id));
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id){
        categoryService.delete(id);
       return ResponseEntity.noContent().build();
   }

}
