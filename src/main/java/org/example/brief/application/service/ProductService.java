package org.example.brief.application.service;

import org.example.brief.application.dto.request.ProductRequest;
import org.example.brief.application.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    Page<ProductResponse> findAll(Pageable pageable);
    Page<ProductResponse> searchProducts(String designation, Long categoryId, Pageable pageable);
    ProductResponse create(ProductRequest productDto);
    ProductResponse update(Long id, ProductRequest productDto);
    void delete(Long id);
}
