package org.example.brief.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.brief.application.dto.request.ProductRequest;
import org.example.brief.application.dto.response.ProductResponse;
import org.example.brief.application.mapper.ProductMapper;
import org.example.brief.application.service.ProductService;
import org.example.brief.domain.model.Product;
import org.example.brief.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductApplicationService implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String designation, Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public ProductResponse create(ProductRequest productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productDto) {
        Product product = productRepository.findById(id).orElse(null);
        productMapper.updateEntity(product, productDto);
        return productMapper.toResponse(product);
    }

    @Override
    public void delete(Long id) {
      productRepository.deleteById(id);
    }
}
