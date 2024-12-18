package org.example.brief.application.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {


    private String designation;


    private Double price;


    private Integer quantity;

  private String description;


    private Long categoryId;
}
