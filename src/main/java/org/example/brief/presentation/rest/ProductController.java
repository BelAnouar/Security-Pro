package org.example.brief.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.brief.application.dto.request.ProductRequest;
import org.example.brief.application.dto.response.ProductResponse;
import org.example.brief.application.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @PageableDefault(size = 20, sort = "designation") Pageable pageable
    ) {
        Page<ProductResponse> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ProductResponse> products = productService.searchProducts(designation, categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productDto
    ) {
        ProductResponse createdProduct = productService.create(productDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productDto
    ) {
        ProductResponse updatedProduct = productService.update(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

