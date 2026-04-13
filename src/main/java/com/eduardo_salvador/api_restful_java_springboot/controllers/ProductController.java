package com.eduardo_salvador.api_restful_java_springboot.controllers;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productRecordDto));
    }

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

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getOneProduct(@PathVariable(value="id") UUID id) {
        ProductResponseDto product0 = productService.findById(id);
        product0.add(linkTo(methodOn(ProductController.class).getAllProducts(null, null, null, null)).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product0);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable(value="id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, productRecordDto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }
}