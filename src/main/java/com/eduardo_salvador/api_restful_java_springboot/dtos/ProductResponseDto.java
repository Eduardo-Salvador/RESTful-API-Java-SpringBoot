package com.eduardo_salvador.api_restful_java_springboot.dtos;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "ProductResponseDto",
        description = "Data transfer object for product response"
)
@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto extends RepresentationModel<ProductResponseDto> {

    @Schema(description = "Unique identifier of the product",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID idProduct;

    @Schema(description = "Name of the product",
            example = "Laptop"
    )
    private String name;

    @Schema(description = "Price of the product",
            example = "999.99"
    )
    @Positive
    private BigDecimal price;
}