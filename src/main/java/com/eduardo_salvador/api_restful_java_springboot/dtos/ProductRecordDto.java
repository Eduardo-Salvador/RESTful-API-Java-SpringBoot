package com.eduardo_salvador.api_restful_java_springboot.dtos;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(name = "ProductRecordDto", description = "Data transfer object for product creation and update")
public record ProductRecordDto(
        @Schema(name = "name",
                description = "Name of the product",
                example = "Laptop",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String name,

        @Schema(name = "price",
                description = "Price of the product",
                example = "999.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        BigDecimal price
) { }