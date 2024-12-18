package org.example.brief.application.mapper;


import org.example.brief.application.dto.request.ProductRequest;
import org.example.brief.application.dto.response.ProductResponse;
import org.example.brief.domain.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest product);
    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product product, ProductRequest productRequest);
}
