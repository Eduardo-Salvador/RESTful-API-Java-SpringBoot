package com.eduardo_salvador.api_restful_java_springboot.controllers;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.services.ProductService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Product API", description = "API for managing products")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(
            summary = "Create a new product",
            description = "Create a new product with the provided product data",
            parameters = {
                    @Parameter(name = "productRecordDto",
                            description = "Product data for creating a new product",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                description = "Product created successfully",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "ProductResponseDto",
                                value = """
                                        {
                                          "idProduct": "123e4567-e89b-12d3-a456-426614174000",
                                          "name": "Laptop",
                                          "price": 999.99,
                                          "_links": {
                                            "self": {
                                              "href": "http://localhost:8080/products/123e4567-e89b-12d3-a456-426614174000"
                                            },
                                            "Products List": {
                                              "href": "http://localhost:8080/products?page=0&size=10&sort=name,asc"
                                            }
                                          }
                                        }
                                """
                        )
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productRecordDto));
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieve a paginated list of products with optional filtering by name and price range",
            parameters = {
                    @Parameter(name = "name",
                            description = "Filter products by name (optional)"
                    ),
                    @Parameter(name = "minPrice",
                            description = "Filter products with a minimum price (optional)"
                    ),
                    @Parameter(name = "maxPrice",
                            description = "Filter products with a maximum price (optional)"
                    ),
                    @Parameter(name = "page",
                            description = "Page number for pagination (default is 0)"
                    ),
                    @Parameter(name = "size",
                            description = "Number of products per page for pagination (default is 10)"
                    ),
                    @Parameter(name = "sort",
                            description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported."
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ProductsListResponse",
                                    value = """
                                            {
                                              "content": [
                                                {
                                                  "idProduct": "123e4567-e89b-12d3-a456-426614174000",
                                                  "name": "Laptop",
                                                  "price": 999.99,
                                                  "_links": {
                                                    "self": {
                                                      "href": "http://localhost:8080/products/123e4567-e89b-12d3-a456-426614174000"
                                                    }
                                                  }
                                                }
                                              ],
                                              "pageable": {
                                                "sort": {
                                                  "sorted": true,
                                                  "unsorted": false,
                                                  "empty": false
                                                },
                                                "pageNumber": 0,
                                                "pageSize": 10,
                                                "offset": 0,
                                                "paged": true,
                                                "unpaged": false
                                              },
                                              "totalPages": 1,
                                              "totalElements": 1,
                                              "last": true,
                                              "size": 10,
                                              "number": 0,
                                              "sort": {
                                                "sorted": true,
                                                "unsorted": false,
                                                "empty": false
                                              },
                                              "numberOfElements": 1,
                                              "first": true,
                                              "empty": false
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductResponseDto> productsList = productService.findAll(name, minPrice, maxPrice, pageable);
        for (ProductResponseDto product : productsList) {
            product.add(linkTo(methodOn(ProductController.class)
                    .getOneProduct(product.getIdProduct()))
                    .withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve a single product by its unique ID",
            parameters = {
                    @Parameter(name = "id",
                            description = "Unique identifier of the product to retrieve",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ProductResponseDto",
                                    value = """
                                            {
                                              "idProduct": "123e4567-e89b-12d3-a456-426614174000",
                                              "name": "Laptop",
                                              "price": 999.99,
                                              "_links": {
                                                "self": {
                                                  "href": "http://localhost:8080/products/123e4567-e89b-12d3-a456-426614174000"
                                                },
                                                "Products List": {
                                                  "href": "http://localhost:8080/products?page=0&size=10&sort=name,asc"
                                                }
                                              }
                                            }
                                            """
                            ))
            ),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getOneProduct(@PathVariable(value="id") UUID id) {
        ProductResponseDto product0 = productService.findById(id);
        product0.add(linkTo(methodOn(ProductController.class).getAllProducts(null, null, null, null)).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product0);
    }


    @Operation(
            summary = "Update a product by ID",
            description = "Update an existing product by its unique ID with the provided product data",
            parameters = {
                    @Parameter(name = "id",
                            description = "Unique identifier of the product to update",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ProductResponseDto",
                                    value = """
                                            {
                                              "idProduct": "123e4567-e89b-12d3-a456-426614174000",
                                              "name": "Updated Laptop",
                                              "price": 899.99,
                                              "_links": {
                                                "self": {
                                                  "href": "http://localhost:8080/products/123e4567-e89b-12d3-a456-426614174000"
                                                },
                                                "Products List": {
                                                  "href": "http://localhost:8080/products?page=0&size=10&sort=name,asc"
                                                }
                                              }
                                            }
                                            """
                            )
                    )
                ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable(value="id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, productRecordDto));
    }

    @Operation(
            summary = "Delete a product by ID",
            description = "Delete an existing product by its unique ID",
            parameters = {
                    @Parameter(name = "id",
                            description = "Unique identifier of the product to delete",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product deleted successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DeleteProductResponse",
                                    value = """
                                            {
                                              "message": "Product deleted successfully."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }
}