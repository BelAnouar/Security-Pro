package org.example.brief.application.mapper;


import org.example.brief.application.dto.request.CategoryRequest;
import org.example.brief.application.dto.response.CategoryResponse;
import org.example.brief.domain.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);

    CategoryResponse toResponse(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void  udpateEntity(CategoryRequest categoryRequest,@MappingTarget Category category);
}
