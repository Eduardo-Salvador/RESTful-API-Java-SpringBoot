package com.eduardo_salvador.api_restful_java_springboot.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto extends RepresentationModel<ProductResponseDto> {
    private UUID idProduct;
    private String name;
    private BigDecimal price;
}